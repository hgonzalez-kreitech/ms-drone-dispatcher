package com.acl.dronedispatcher.repository.impl;

import com.acl.dronedispatcher.domain.Medication;
import com.acl.dronedispatcher.repository.mapper.MedicationRowMapper;
import com.acl.dronedispatcher.service.exception.ClientException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

class MedicationRepositoryImplTest {

    private JdbcTemplate jdbcTemplate;
    private MedicationRepositoryImpl medicationRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        medicationRepository = new MedicationRepositoryImpl(jdbcTemplate);
    }

    @Test
    void testCreateMedication_Success() throws SQLException {
        Medication medication = new Medication();
        medication.setName("Medication");
        medication.setCode("MED123");
        medication.setWeight(100);

        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(jdbcTemplate.getDataSource()).thenReturn(Mockito.mock(javax.sql.DataSource.class));
        Mockito.when(jdbcTemplate.getDataSource().getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.doNothing().when(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(), Mockito.anyInt());
        Mockito.doReturn(1).when(preparedStatement).executeUpdate();

        Optional<Medication> result = medicationRepository.createMedication(medication);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(medication, result.get());
    }

    @Test
    void testCreateMedication_Error() throws SQLException {
        Medication medication = new Medication();
        medication.setName("Medication");
        medication.setCode("MED123");
        medication.setWeight(100);

        Mockito.when(jdbcTemplate.getDataSource()).thenReturn(Mockito.mock(javax.sql.DataSource.class));
        Mockito.when(jdbcTemplate.getDataSource().getConnection()).thenThrow(Mockito.mock(SQLException.class));

        Optional<Medication> result = medicationRepository.createMedication(medication);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testUpdateMedicationBatch_Success() {
        List<Medication> medications = Arrays.asList(
                createMedication(1, 1),
                createMedication(1, 1)
        );

        Mockito.when(jdbcTemplate.batchUpdate(Mockito.anyString(), Mockito.any(BatchPreparedStatementSetter.class)))
                .thenReturn(new int[]{1, 1});

        Optional<int[]> result = medicationRepository.updateMedicationBatch(medications);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertArrayEquals(new int[]{1, 1}, result.get());

        Mockito.verify(jdbcTemplate).batchUpdate(Mockito.anyString(), Mockito.any(BatchPreparedStatementSetter.class));
    }

    @Test
    void testUpdateMedicationBatch_Error() {
        List<Medication> medications = Arrays.asList(
                createMedication(1, 1),
                createMedication(2, 1)
        );

        Mockito.when(jdbcTemplate.batchUpdate(Mockito.anyString(), Mockito.any(BatchPreparedStatementSetter.class)))
                .thenThrow(Mockito.mock(DataAccessException.class));

        Optional<int[]> result = medicationRepository.updateMedicationBatch(medications);

        Assertions.assertFalse(result.isPresent());

        Mockito.verify(jdbcTemplate).batchUpdate(Mockito.anyString(), Mockito.any(BatchPreparedStatementSetter.class));
    }

    @Test
    void testGetMedication_Success() {
        Integer id = 1;
        Medication expectedMedication = createMedication(id, 1);

        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(MedicationRowMapper.class)))
                .thenReturn(expectedMedication);

        Optional<Medication> result = medicationRepository.getMedication(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedMedication, result.get());

        Mockito.verify(jdbcTemplate).queryForObject(Mockito.anyString(), Mockito.any(MedicationRowMapper.class));
    }

    @Test
    void testGetMedication_NotFound() {
        Integer id = 1;

        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(MedicationRowMapper.class)))
                .thenThrow(Mockito.mock(DataAccessException.class));

        Assertions.assertThrows(ClientException.class, () -> medicationRepository.getMedication(id));

        Mockito.verify(jdbcTemplate).queryForObject(Mockito.anyString(), Mockito.any(MedicationRowMapper.class));
    }

    @Test
    void testGetMedicationsByDrone_Success() {
        Integer droneId = 1;
        List<Medication> expectedMedications = Arrays.asList(
                createMedication(1, droneId),
                createMedication(2, droneId)
        );

        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(MedicationRowMapper.class)))
                .thenReturn(expectedMedications);

        Optional<List<Medication>> result = medicationRepository.getMedicationsByDrone(droneId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedMedications, result.get());

        Mockito.verify(jdbcTemplate).query(Mockito.anyString(), Mockito.any(MedicationRowMapper.class));
    }

    @Test
    void testGetMedicationsByDrone_Error() {
        Integer droneId = 1;

        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(MedicationRowMapper.class)))
                .thenThrow(Mockito.mock(DataAccessException.class));

        Assertions.assertThrows(ClientException.class, () -> medicationRepository.getMedicationsByDrone(droneId));

        Mockito.verify(jdbcTemplate).query(Mockito.anyString(), Mockito.any(MedicationRowMapper.class));
    }

    private Medication createMedication(Integer id, Integer droneId) {
        Medication medication = new Medication();
        medication.setId(id);
        medication.setDroneId(droneId);
        return medication;
    }
}