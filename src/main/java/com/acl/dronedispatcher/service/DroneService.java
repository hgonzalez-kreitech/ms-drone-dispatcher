package com.acl.dronedispatcher.service;

import com.acl.dronedispatcher.controller.model.DroneLoadRequest;
import com.acl.dronedispatcher.controller.model.DroneRequest;
import com.acl.dronedispatcher.controller.model.DroneResponse;
import com.acl.dronedispatcher.controller.model.MedicationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DroneService {
    Mono<DroneResponse> createDrone(DroneRequest droneRequest);
    Mono<Void> loadDrone(DroneLoadRequest droneLoadRequest);
    Flux<MedicationResponse> getDroneLoadedMedications(Integer droneId);
    Flux<DroneResponse> getDronesAvailableForLoading();
    Mono<Integer> getDroneBatteryLevel(Integer droneId);
    void logDronesCapacity();
}
