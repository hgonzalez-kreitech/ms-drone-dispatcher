package com.acl.dronedispatcher.controller;

import com.acl.dronedispatcher.controller.model.MedicationRequest;
import com.acl.dronedispatcher.controller.model.MedicationResponse;
import com.acl.dronedispatcher.service.MedicationService;
import com.acl.dronedispatcher.service.exception.ClientException;
import com.acl.dronedispatcher.service.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MedicationControllerTest {

    @Test
    void testCreateMedication() {
        MedicationRequest request = new MedicationRequest();
        MedicationResponse response = new MedicationResponse();
        MedicationService medicationService = mock(MedicationService.class);
        when(medicationService.createMedication(request)).thenReturn(Mono.just(response));

        MedicationController controller = new MedicationController(medicationService);
        Mono<MedicationResponse> result = controller.createMedication(request);

        StepVerifier.create(result)
                .expectNext(response)
                .verifyComplete();

        verify(medicationService).createMedication(request);
    }

    @Test
    void testCreateMedicationWithError() {
        MedicationRequest request = new MedicationRequest();
        MedicationService medicationService = mock(MedicationService.class);
        when(medicationService.createMedication(request)).thenReturn(Mono.error(new ClientException(ErrorCode.MEDICATION_CREATION_FAILED)));

        MedicationController controller = new MedicationController(medicationService);
        Mono<MedicationResponse> result = controller.createMedication(request);

        StepVerifier.create(result)
                .expectError(ClientException.class)
                .verify();

        verify(medicationService).createMedication(request);
    }
}