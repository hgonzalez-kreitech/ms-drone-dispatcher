package com.acl.dronedispatcher.repository.impl;

import com.acl.dronedispatcher.domain.Medication;
import com.acl.dronedispatcher.repository.MedicationRepository;
import com.acl.dronedispatcher.repository.mapper.MedicationRowMapper;
import com.acl.dronedispatcher.service.exception.ClientException;
import com.acl.dronedispatcher.service.exception.ErrorCode;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MedicationRepositoryImpl implements MedicationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public Optional<Medication> createMedication(Medication medication) {
        try {
            log.info("MedicationRepositoryImpl.createMedication : {}", medication.toString());
            var query = "INSERT INTO medications (name, code, weight, image)"
                    + "VALUES (?, ?, ?, ?)";

            try (var con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
                PreparedStatementCreator psc = conn -> {
                    PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, medication.getName());
                    ps.setString(2, medication.getCode());
                    ps.setInt(3, medication.getWeight());
                    ps.setBytes(4, null);
                    return ps;
                };
                // Create a KeyHolder to store the generated keys
                KeyHolder keyHolder = new GeneratedKeyHolder();

                // Execute the update and retrieve the generated key
                jdbcTemplate.update(psc, keyHolder);
                // Retrieve the generated key
                keyHolder.getKeyList()
                        .forEach(mapKeys -> medication.setId((Integer) mapKeys.get("id")));
                return Optional.of(medication);
            }
        } catch (DataAccessException | SQLException e) {
            log.error("Error creating medication in database {}", e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<int[]> updateMedicationBatch(List<Medication> medications) {
        try {
            return Optional.of(jdbcTemplate.batchUpdate(
                "UPDATE medications SET drone_id = ? WHERE id = ?",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, medications.get(i).getDroneId());
                        ps.setInt(2, medications.get(i).getId());
                    }
                    public int getBatchSize() {
                        return medications.size();
                    }
                }));
        } catch (DataAccessException e) {
            log.error("Error updating medications in the database {} ", e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Medication> getMedication(Integer medicationId) {
        try {
            log.info("MedicationRepositoryImpl.getMedication: {}", medicationId);
            var query = "SELECT id, name, code, weight, image, drone_id FROM medications WHERE id = " + medicationId + "";
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, new MedicationRowMapper()));
        } catch (DataAccessException e) {
            log.error("Error getting medication in database ", e);
            throw new ClientException(ErrorCode.MEDICATION_CODE_NOT_FOUND);
        }
    }

    @Override
    public Optional<List<Medication>> getMedicationsByDrone(Integer droneId) {
        try {
            log.info("MedicationRepositoryImpl.getMedicationsByDrone: {}", droneId);
            var query = "SELECT id, name, code, weight, image, drone_id FROM medications WHERE drone_id = " + droneId + "";
            return Optional.of(jdbcTemplate.query(query, new MedicationRowMapper()));
        } catch (DataAccessException e) {
            log.error("Error getting drone loaded medications in database ", e);
            throw new ClientException(ErrorCode.DRONE_LOADED_MEDICATIONS_QUERY_FAILED);
        }
    }
}
