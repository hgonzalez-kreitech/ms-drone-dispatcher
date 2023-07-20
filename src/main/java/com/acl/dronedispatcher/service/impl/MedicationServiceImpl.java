package com.acl.dronedispatcher.service.impl;

import com.acl.dronedispatcher.controller.model.MedicationRequest;
import com.acl.dronedispatcher.controller.model.MedicationResponse;
import com.acl.dronedispatcher.repository.MedicationRepository;
import com.acl.dronedispatcher.service.MedicationService;
import com.acl.dronedispatcher.service.exception.ClientException;
import com.acl.dronedispatcher.service.exception.ErrorCode;
import com.acl.dronedispatcher.utils.mapper.MedicationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;

    @Override
    public Mono<MedicationResponse> createMedication(MedicationRequest medicationRequest) {
        log.info("[create medication] -> " + medicationRequest.getCode());
        return medicationRepository.createMedication(medicationMapper.createMedicationRequest(medicationRequest))
                .map(medicationCreated -> Mono.just(medicationMapper.createMedicationResponse(medicationCreated)))
                .orElse(Mono.error(new ClientException(ErrorCode.MEDICATION_CREATION_FAILED)));
    }
}
