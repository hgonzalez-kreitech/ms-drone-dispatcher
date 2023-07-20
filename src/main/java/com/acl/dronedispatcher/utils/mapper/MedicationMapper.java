package com.acl.dronedispatcher.utils.mapper;

import com.acl.dronedispatcher.controller.model.MedicationRequest;
import com.acl.dronedispatcher.controller.model.MedicationResponse;
import com.acl.dronedispatcher.domain.Medication;
import org.springframework.stereotype.Component;

@Component
public class MedicationMapper {

  /**
   * Medication to MedicationResponse.
   * @param medication Medication to response
   * @return MedicationResponse
   */
  public MedicationResponse createMedicationResponse(Medication medication) {
    return MedicationResponse.builder()
            .droneId(medication.getDroneId())
            .code(medication.getCode())
            .weight(medication.getWeight())
            .image(medication.getImage())
            .name(medication.getName())
            .id(medication.getId())
            .build();
  }

  /**
   * MedicationRequest to Medication.
   * @param medicationRequest Medication to Create
   * @return Medication
   */
  public Medication createMedicationRequest(MedicationRequest medicationRequest) {
    return Medication.builder()
            .code(medicationRequest.getCode())
            .weight(medicationRequest.getWeight())
            .image(medicationRequest.getImage())
            .name(medicationRequest.getName())
            .build();
  }
}
