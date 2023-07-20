package com.acl.dronedispatcher.utils.mapper;

import com.acl.dronedispatcher.controller.model.MedicationRequest;
import com.acl.dronedispatcher.domain.Medication;
import org.junit.jupiter.api.Test;

class MedicationMapperTest {

    @Test
    void createMedicationResponse() {
        MedicationMapper medicationMapper = new MedicationMapper();
        Medication medication = Medication.builder().build();
        medicationMapper.createMedicationResponse(medication);
        assert true;
    }

    @Test
    void createDroneResponse() {
        MedicationMapper medicationMapper = new MedicationMapper();
        MedicationRequest medication = MedicationRequest.builder().build();
        medicationMapper.createMedicationRequest(medication);
        assert true;
    }
}