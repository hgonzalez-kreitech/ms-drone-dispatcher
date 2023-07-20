package com.acl.dronedispatcher.service.impl;

import com.acl.dronedispatcher.controller.model.MedicationRequest;
import com.acl.dronedispatcher.controller.model.MedicationResponse;
import com.acl.dronedispatcher.domain.Medication;
import com.acl.dronedispatcher.repository.MedicationRepository;
import com.acl.dronedispatcher.service.exception.ClientException;
import com.acl.dronedispatcher.utils.mapper.MedicationMapper;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MedicationServiceImplTest {

    @Test
    void testCreateMedication() {
        MedicationRequest request = new MedicationRequest();
        MedicationResponse response = new MedicationResponse();
        MedicationRepository medicationRepository = mock(MedicationRepository.class);
        MedicationMapper medicationMapper = mock(MedicationMapper.class);
        when(medicationRepository.createMedication(any())).thenReturn(Optional.of(new Medication()));
        when(medicationMapper.createMedicationResponse(any())).thenReturn(response);

        MedicationServiceImpl service = new MedicationServiceImpl(medicationRepository, medicationMapper);
        Mono<MedicationResponse> result = service.createMedication(request);

        StepVerifier.create(result)
                .expectNext(response)
                .verifyComplete();

        verify(medicationRepository).createMedication(any());
        verify(medicationMapper).createMedicationResponse(any());
    }

    @Test
    void testCreateMedicationWithCreationFailed() {
        MedicationRequest request = new MedicationRequest();
        MedicationRepository medicationRepository = mock(MedicationRepository.class);
        MedicationMapper medicationMapper = mock(MedicationMapper.class);
        when(medicationRepository.createMedication(any())).thenReturn(Optional.empty());

        MedicationServiceImpl service = new MedicationServiceImpl(medicationRepository, medicationMapper);
        Mono<MedicationResponse> result = service.createMedication(request);

        StepVerifier.create(result)
                .expectError(ClientException.class)
                .verify();

        verify(medicationRepository).createMedication(any());
        verify(medicationMapper, never()).createMedicationResponse(any());
    }
}