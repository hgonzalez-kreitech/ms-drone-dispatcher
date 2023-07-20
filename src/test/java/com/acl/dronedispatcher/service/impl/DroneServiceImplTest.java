package com.acl.dronedispatcher.service.impl;

import com.acl.dronedispatcher.controller.model.DroneLoadRequest;
import com.acl.dronedispatcher.controller.model.DroneRequest;
import com.acl.dronedispatcher.controller.model.DroneResponse;
import com.acl.dronedispatcher.controller.model.MedicationResponse;
import com.acl.dronedispatcher.domain.Drone;
import com.acl.dronedispatcher.domain.Medication;
import com.acl.dronedispatcher.domain.enums.StateEnum;
import com.acl.dronedispatcher.repository.DroneRepository;
import com.acl.dronedispatcher.repository.MedicationRepository;
import com.acl.dronedispatcher.service.exception.ClientException;
import com.acl.dronedispatcher.utils.mapper.DroneMapper;
import com.acl.dronedispatcher.utils.mapper.MedicationMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DroneServiceImplTest {

    @Test
    void testCreateDrone() {
        DroneRequest request = new DroneRequest();
        DroneResponse response = new DroneResponse();
        DroneRepository droneRepository = mock(DroneRepository.class);
        DroneMapper droneMapper = mock(DroneMapper.class);
        when(droneRepository.droneCount()).thenReturn(0);
        when(droneRepository.createDrone(any())).thenReturn(Optional.of(new Drone()));
        when(droneMapper.createDroneResponse(any())).thenReturn(response);

        DroneServiceImpl service = new DroneServiceImpl(droneRepository, null, droneMapper, null);
        Mono<DroneResponse> result = service.createDrone(request);

        StepVerifier.create(result)
                .expectNext(response)
                .verifyComplete();

        verify(droneRepository).droneCount();
        verify(droneRepository).createDrone(any());
        verify(droneMapper).createDroneResponse(any());
    }

    @Test
    void testCreateDroneWithExceededDroneFleetLimit() {
        DroneRequest request = new DroneRequest();
        DroneRepository droneRepository = mock(DroneRepository.class);
        when(droneRepository.droneCount()).thenReturn(11);

        DroneServiceImpl service = new DroneServiceImpl(droneRepository, null, null, null);
        Mono<DroneResponse> result = service.createDrone(request);

        StepVerifier.create(result)
                .expectError(ClientException.class)
                .verify();

        verify(droneRepository).droneCount();
        verify(droneRepository, never()).createDrone(any());
    }

    @Test
    void testCreateDroneWithCreationFailed() {
        DroneRequest request = new DroneRequest();
        DroneRepository droneRepository = mock(DroneRepository.class);
        DroneMapper droneMapper = mock(DroneMapper.class);
        when(droneRepository.droneCount()).thenReturn(0);
        when(droneRepository.createDrone(any())).thenReturn(Optional.empty());

        DroneServiceImpl service = new DroneServiceImpl(droneRepository, null, droneMapper, null);
        Mono<DroneResponse> result = service.createDrone(request);

        StepVerifier.create(result)
                .expectError(ClientException.class)
                .verify();

        verify(droneRepository).droneCount();
        verify(droneRepository).createDrone(any());
        verify(droneMapper, never()).createDroneResponse(any());
    }

    @Test
    void testLoadDrone() {
        DroneLoadRequest request = new DroneLoadRequest();
        request.setDroneId(1);
        List<Integer> medications = new ArrayList<>();
        medications.add(1);
        request.setMedications(medications);
        int[] response = new int[2];
        DroneRepository droneRepository = mock(DroneRepository.class);
        MedicationRepository medicationRepository = mock(MedicationRepository.class);
        Drone drone = new Drone();
        drone.setWeight(100);
        drone.setState(StateEnum.IDLE.getState());
        drone.setModel("Heavyweight");
        drone.setId(1);
        drone.setSerialNumber("SERIAL");
        drone.setBatteryCapacity(90);
        when(droneRepository.getDrone(request.getDroneId())).thenReturn(Optional.of(drone));
        Medication medication = new Medication();
        medication.setWeight(10);
        medication.setDroneId(0);
        when(medicationRepository.getMedication(1)).thenReturn(Optional.of(medication));
        when(droneRepository.updateDrone(drone)).thenReturn(Optional.of(drone));
        when(medicationRepository.updateMedicationBatch(any())).thenReturn(Optional.of(response));

        DroneServiceImpl service = new DroneServiceImpl(droneRepository, medicationRepository, null, null);
        Mono<Void> result = service.loadDrone(request);

        StepVerifier.create(result)
                .verifyComplete();

        verify(droneRepository).getDrone(request.getDroneId());
        verify(medicationRepository).getMedication(1);
        verify(droneRepository).updateDrone(drone);
        verify(medicationRepository).updateMedicationBatch(any());
    }

    @Test
    void testLoadDroneNewWeightEqualsMaxWeight() {
        DroneLoadRequest request = new DroneLoadRequest();
        request.setDroneId(1);
        List<Integer> medications = new ArrayList<>();
        medications.add(1);
        request.setMedications(medications);
        int[] response = new int[2];
        DroneRepository droneRepository = mock(DroneRepository.class);
        MedicationRepository medicationRepository = mock(MedicationRepository.class);
        Drone drone = new Drone();
        drone.setWeight(100);
        drone.setState(StateEnum.IDLE.getState());
        drone.setModel("Heavyweight");
        drone.setId(1);
        drone.setSerialNumber("SERIAL");
        drone.setBatteryCapacity(90);
        when(droneRepository.getDrone(request.getDroneId())).thenReturn(Optional.of(drone));
        Medication medication = new Medication();
        medication.setWeight(400);
        medication.setDroneId(0);
        when(medicationRepository.getMedication(1)).thenReturn(Optional.of(medication));
        when(droneRepository.updateDrone(drone)).thenReturn(Optional.of(drone));
        when(medicationRepository.updateMedicationBatch(any())).thenReturn(Optional.of(response));

        DroneServiceImpl service = new DroneServiceImpl(droneRepository, medicationRepository, null, null);
        Mono<Void> result = service.loadDrone(request);

        StepVerifier.create(result)
                .verifyComplete();

        verify(droneRepository).getDrone(request.getDroneId());
        verify(medicationRepository).getMedication(1);
        verify(droneRepository).updateDrone(drone);
        verify(medicationRepository).updateMedicationBatch(any());
    }

    @Test
    void testGetDroneLoadedMedications() {
        Integer droneId = 1;
        Medication medication = new Medication();
        medication.setDroneId(droneId);
        MedicationResponse response = new MedicationResponse();
        DroneRepository droneRepository = mock(DroneRepository.class);
        MedicationRepository medicationRepository = mock(MedicationRepository.class);
        when(droneRepository.getDrone(droneId)).thenReturn(Optional.of(new Drone()));
        when(medicationRepository.getMedicationsByDrone(droneId)).thenReturn(Optional.of(List.of(medication)));
        MedicationMapper medicationMapper = mock(MedicationMapper.class);
        when(medicationMapper.createMedicationResponse(medication)).thenReturn(response);

        DroneServiceImpl service = new DroneServiceImpl(droneRepository, medicationRepository, null, medicationMapper);
        Flux<MedicationResponse> result = service.getDroneLoadedMedications(droneId);

        StepVerifier.create(result)
                .expectNext(response)
                .verifyComplete();

        verify(droneRepository).getDrone(droneId);
        verify(medicationRepository).getMedicationsByDrone(droneId);
        verify(medicationMapper).createMedicationResponse(medication);
    }

    @Test
    void testGetDroneLoadedMedicationsWithEmptyDrone() {
        Integer droneId = 1;
        DroneRepository droneRepository = mock(DroneRepository.class);
        when(droneRepository.getDrone(droneId)).thenReturn(Optional.empty());
        MedicationRepository medicationRepository = mock(MedicationRepository.class);
        MedicationMapper medicationMapper = mock(MedicationMapper.class);
        DroneServiceImpl service = new DroneServiceImpl(droneRepository, null, null, null);
        Flux<MedicationResponse> result = service.getDroneLoadedMedications(droneId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(droneRepository).getDrone(droneId);
        verifyNoInteractions(medicationRepository);
        verifyNoInteractions(medicationMapper);
    }

    @Test
    void testGetDronesAvailableForLoading() {
        List<Drone> drones = new ArrayList<>();
        var drone = Drone.builder()
                .batteryCapacity(30).build();
        drones.add(drone);
        DroneRepository droneRepository = mock(DroneRepository.class);
        when(droneRepository.getDronesByStatesAndWeight(any(), anyInt())).thenReturn(Optional.of(drones));
        DroneMapper droneMapper = mock(DroneMapper.class);
        DroneResponse response = new DroneResponse();
        when(droneMapper.createDroneResponse(any())).thenReturn(response);

        DroneServiceImpl service = new DroneServiceImpl(droneRepository, null, droneMapper, null);
        Flux<DroneResponse> result = service.getDronesAvailableForLoading();

        StepVerifier.create(result)
                .expectNext(response)
                .verifyComplete();

        verify(droneRepository).getDronesByStatesAndWeight(any(), anyInt());
        verify(droneMapper).createDroneResponse(any());
    }

    @Test
    void testGetDronesAvailableForLoadingWithEmptyDrones() {
        DroneRepository droneRepository = mock(DroneRepository.class);
        when(droneRepository.getDronesByStatesAndWeight(any(), anyInt())).thenReturn(Optional.empty());
        DroneMapper droneMapper = mock(DroneMapper.class);
        DroneServiceImpl service = new DroneServiceImpl(droneRepository, null, null, null);
        Flux<DroneResponse> result = service.getDronesAvailableForLoading();

        StepVerifier.create(result)
                .verifyComplete();

        verify(droneRepository).getDronesByStatesAndWeight(any(), anyInt());
        verifyNoInteractions(droneMapper);
    }

    @Test
    void testGetDroneBatteryLevel() {
        Integer droneId = 1;
        DroneRepository droneRepository = mock(DroneRepository.class);
        Drone drone = new Drone();
        drone.setBatteryCapacity(80);
        when(droneRepository.getDrone(droneId)).thenReturn(Optional.of(drone));

        DroneServiceImpl service = new DroneServiceImpl(droneRepository, null, null, null);
        Mono<Integer> result = service.getDroneBatteryLevel(droneId);

        StepVerifier.create(result)
                .expectNext(80)
                .verifyComplete();

        verify(droneRepository).getDrone(droneId);
    }

    @Test
    void testGetDroneBatteryLevelWithEmptyDrone() {
        Integer droneId = 1;
        DroneRepository droneRepository = mock(DroneRepository.class);
        when(droneRepository.getDrone(droneId)).thenReturn(Optional.empty());

        DroneServiceImpl service = new DroneServiceImpl(droneRepository, null, null, null);
        Mono<Integer> result = service.getDroneBatteryLevel(droneId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(droneRepository).getDrone(droneId);
    }

    @Test
    void testLogDronesCapacity() {
        // Mock the list of drones
        var drone = Drone.builder()
                .batteryCapacity(100)
                .serialNumber("serial1")
                .build();
        var drone2 = Drone.builder()
                .batteryCapacity(80)
                .serialNumber("serial2")
                .build();
        List<Drone> drones = Arrays.asList(
                drone,
                drone2
        );
        Optional<List<Drone>> optionalDrones = Optional.of(drones);

        // Mock the the logger
        Logger logger = mock(Logger.class);
        // Mock the behavior of droneRepository.getAllDrones()
        DroneRepository droneRepository = mock(DroneRepository.class);
        Mockito.when(droneRepository.getAllDrones()).thenReturn(optionalDrones);

        // Create an instance of DroneServiceImpl
        DroneServiceImpl droneService = new DroneServiceImpl(droneRepository,null,null,null);

        // Call the logDronesCapacity() method
        assertDoesNotThrow(droneService::logDronesCapacity);

    }
}
