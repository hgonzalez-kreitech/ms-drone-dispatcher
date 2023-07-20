package com.acl.dronedispatcher.service;

import com.acl.dronedispatcher.controller.model.MedicationRequest;
import com.acl.dronedispatcher.controller.model.MedicationResponse;
import reactor.core.publisher.Mono;

public interface MedicationService {
    Mono<MedicationResponse> createMedication(MedicationRequest medicationRequest);
}
