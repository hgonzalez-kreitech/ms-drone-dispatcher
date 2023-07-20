package com.acl.dronedispatcher.controller;

import com.acl.dronedispatcher.controller.model.DroneLoadRequest;
import com.acl.dronedispatcher.controller.model.DroneRequest;
import com.acl.dronedispatcher.controller.model.DroneResponse;
import com.acl.dronedispatcher.controller.model.MedicationResponse;
import com.acl.dronedispatcher.service.DroneService;
import com.acl.dronedispatcher.service.exception.ClientException;
import com.acl.dronedispatcher.service.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DroneControllerTest {

    @Test
    void testCreateDrone() {
        DroneRequest request = new DroneRequest();
        DroneResponse response = new DroneResponse();
        DroneService droneService = mock(DroneService.class);
        when(droneService.createDrone(request)).thenReturn(Mono.just(response));

        DroneController controller = new DroneController(droneService);
        Mono<DroneResponse> result = controller.createDrone(request);

        StepVerifier.create(result)
                .expectNext(response)
                .verifyComplete();

        verify(droneService).createDrone(request);
    }

    @Test
    void testCreateDroneWithError() {
        DroneRequest request = new DroneRequest();
        DroneService droneService = mock(DroneService.class);
        when(droneService.createDrone(request)).thenReturn(Mono.error(new ClientException(ErrorCode.DRONE_CREATION_FAILED)));

        DroneController controller = new DroneController(droneService);
        Mono<DroneResponse> result = controller.createDrone(request);

        StepVerifier.create(result)
                .expectError(ClientException.class)
                .verify();

        verify(droneService).createDrone(request);
    }

    @Test
    void testLoadDrone() {
        DroneLoadRequest request = new DroneLoadRequest();
        DroneService droneService = mock(DroneService.class);
        when(droneService.loadDrone(request)).thenReturn(Mono.empty());

        DroneController controller = new DroneController(droneService);
        Mono<Void> result = controller.loadDrone(request);

        StepVerifier.create(result)
                .verifyComplete();

        verify(droneService).loadDrone(request);
    }

    @Test
    void testGetDroneLoadedMedications() {
        Integer droneId = 1;
        MedicationResponse response = new MedicationResponse();
        DroneService droneService = mock(DroneService.class);
        when(droneService.getDroneLoadedMedications(droneId)).thenReturn(Flux.just(response));

        DroneController controller = new DroneController(droneService);
        Flux<MedicationResponse> result = controller.getDroneLoadedMedications(droneId);

        StepVerifier.create(result)
                .expectNext(response)
                .verifyComplete();

        verify(droneService).getDroneLoadedMedications(droneId);
    }

    @Test
    void testGetDronesAvailableForLoading() {
        DroneResponse response = new DroneResponse();
        DroneService droneService = mock(DroneService.class);
        when(droneService.getDronesAvailableForLoading()).thenReturn(Flux.just(response));

        DroneController controller = new DroneController(droneService);
        Flux<DroneResponse> result = controller.getDronesAvailableForLoading();

        StepVerifier.create(result)
                .expectNext(response)
                .verifyComplete();

        verify(droneService).getDronesAvailableForLoading();
    }

    @Test
    void testGetDroneBatteryLevel() {
        Integer droneId = 1;
        int batteryLevel = 80;
        DroneService droneService = mock(DroneService.class);
        when(droneService.getDroneBatteryLevel(droneId)).thenReturn(Mono.just(batteryLevel));

        DroneController controller = new DroneController(droneService);
        Mono<Integer> result = controller.getDroneBatteryLevel(droneId);

        StepVerifier.create(result)
                .expectNext(batteryLevel)
                .verifyComplete();

        verify(droneService).getDroneBatteryLevel(droneId);
    }
}