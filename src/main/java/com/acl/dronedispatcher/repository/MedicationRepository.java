package com.acl.dronedispatcher.repository;

import com.acl.dronedispatcher.domain.Medication;
import java.util.List;
import java.util.Optional;

public interface MedicationRepository {
    Optional<Medication> createMedication(Medication medication);
    Optional<int[]> updateMedicationBatch(List<Medication> medication);
    Optional<Medication> getMedication(Integer medicationId);
    Optional<List<Medication>> getMedicationsByDrone(Integer droneId);
}
