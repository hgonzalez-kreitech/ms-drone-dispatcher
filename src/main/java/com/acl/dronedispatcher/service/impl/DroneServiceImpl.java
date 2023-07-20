package com.acl.dronedispatcher.service.impl;

import com.acl.dronedispatcher.controller.model.DroneLoadRequest;
import com.acl.dronedispatcher.controller.model.DroneRequest;
import com.acl.dronedispatcher.controller.model.DroneResponse;
import com.acl.dronedispatcher.controller.model.MedicationResponse;
import com.acl.dronedispatcher.domain.Medication;
import com.acl.dronedispatcher.domain.enums.StateEnum;
import com.acl.dronedispatcher.repository.DroneRepository;
import com.acl.dronedispatcher.repository.MedicationRepository;
import com.acl.dronedispatcher.service.DroneService;
import com.acl.dronedispatcher.service.exception.ClientException;
import com.acl.dronedispatcher.service.exception.ErrorCode;
import com.acl.dronedispatcher.utils.mapper.DroneMapper;
import com.acl.dronedispatcher.utils.mapper.MedicationMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.acl.dronedispatcher.utils.Constants.DRONE_FLEET_LIMIT;
import static com.acl.dronedispatcher.utils.Constants.MIN_BATTERY_LEVEL;
import static com.acl.dronedispatcher.utils.Constants.WEIGHT_LIMIT;

@Service
@RequiredArgsConstructor
@Slf4j
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final DroneMapper droneMapper;
    private final MedicationMapper medicationMapper;

    @Override
    public Mono<DroneResponse> createDrone(DroneRequest droneRequest) {
        return Mono.just(droneRepository.droneCount())
                .filter(droneCount -> droneCount < DRONE_FLEET_LIMIT)
                .switchIfEmpty(Mono.error(new ClientException(ErrorCode.DRONE_FLEET_SIZE_EXCEEDED)))
                .doOnNext(d -> log.info("[create drone] -> " + droneRequest.getSerialNumber()))
                .flatMap(count -> droneRepository.createDrone(droneMapper.createDroneRequest(droneRequest))
                        .map(droneCreated -> Mono.just(droneMapper.createDroneResponse(droneCreated)))
                        .orElse(Mono.error(new ClientException(ErrorCode.DRONE_CREATION_FAILED))));
    }

    @Override
    public Mono<Void> loadDrone(DroneLoadRequest droneLoadRequest) {
        return Mono.just(droneRepository.getDrone(droneLoadRequest.getDroneId())
                .map(drone -> {
                    List<Medication> medications = new ArrayList<>();
                    droneLoadRequest.getMedications().forEach(medicationId ->
                            medicationRepository.getMedication(medicationId)
                                    .map(medication -> {
                                        if (medication.getDroneId() == 0) {
                                            int newWeight = drone.getWeight() + medication.getWeight();
                                            if (newWeight < WEIGHT_LIMIT) {
                                                drone.setWeight(newWeight);
                                                if (drone.getBatteryCapacity() < MIN_BATTERY_LEVEL) {
                                                    throw new ClientException(ErrorCode.BATTERY_LEVEL_ERROR);
                                                }
                                                drone.setState(StateEnum.LOADING.getState());
                                            }
                                            if (newWeight == WEIGHT_LIMIT) {
                                                drone.setWeight(newWeight);
                                                drone.setState(StateEnum.LOADED.getState());
                                            }
                                            if (newWeight > WEIGHT_LIMIT) {
                                                throw new ClientException(ErrorCode.MEDICATION_OVERLOAD);
                                            }
                                            medication.setDroneId(drone.getId());
                                            medications.add(medication);
                                        }
                                        return Mono.empty();
                                    })
                    );
                    return Mono.zip(Mono.just(medicationRepository.updateMedicationBatch(medications)),
                            Mono.just(droneRepository.updateDrone(drone).map(Mono::just)))
                            .flatMap(data -> Mono.empty());
                })
        ).then();
    }

    @Override
    public Flux<MedicationResponse> getDroneLoadedMedications(Integer droneId) {
        return Mono.fromCallable(() -> droneRepository.getDrone(droneId))
            .flatMapMany(optionalDrone -> optionalDrone
                .map(drone -> Mono.fromCallable(() -> medicationRepository.getMedicationsByDrone(droneId))
                    .flatMapMany(optionalMedications -> optionalMedications
                        .map(medications -> Flux.fromIterable(medications)
                            .map(medicationMapper::createMedicationResponse))
                        .orElse(Flux.empty())))
                .orElse(Flux.empty()));
    }

    @Override
    public Flux<DroneResponse> getDronesAvailableForLoading() {
        var states = "'" + StateEnum.IDLE.getState() + "'" + "," + "'" + StateEnum.LOADING.getState() + "'";
        return Mono.fromCallable(() -> droneRepository.getDronesByStatesAndWeight(states, WEIGHT_LIMIT))
            .flatMapMany(optionalDrones -> optionalDrones
            .map(drones -> Flux.fromIterable(drones
                .stream().filter(drone -> drone.getBatteryCapacity() > MIN_BATTERY_LEVEL)
                .map(droneMapper::createDroneResponse)
                .collect(Collectors.toList())))
            .orElse(Flux.empty()));
    }

    @Override
    public Mono<Integer> getDroneBatteryLevel(Integer droneId) {
        return Mono.fromCallable(() -> droneRepository.getDrone(droneId))
                .flatMap(optionalDrone -> optionalDrone
                .map(drone -> Mono.just(drone.getBatteryCapacity()))
                .orElse(Mono.empty()));
    }

    @Scheduled(fixedRateString = "${scheduler.interval}")
    @Async
    public void logDronesCapacity() {
        log.info("Drone capacity check at " + formatEventDate());
        var optionalListDrones = droneRepository.getAllDrones();
        optionalListDrones.ifPresent(listDrones -> listDrones
                .forEach(drone -> log.info("drone id - {}, serial number - {}, capacity - {}",
                        drone.getId(), drone.getSerialNumber(), drone.getBatteryCapacity())));
    }

    private  String formatEventDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm");
        return formatter.format(LocalDateTime.now());
    }
}
