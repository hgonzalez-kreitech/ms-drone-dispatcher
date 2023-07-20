package com.acl.dronedispatcher.repository.mapper;

import com.acl.dronedispatcher.domain.Medication;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.RowMapper;

class MedicationRowMapperTest {

    @Test
    void testMapRow() throws SQLException {
        // Prepare the ResultSet mock
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getString("code")).thenReturn("MEDICATION_CODE");
        Mockito.when(resultSet.getInt("weight")).thenReturn(5);
        Mockito.when(resultSet.getInt("drone_id")).thenReturn(1);
        Mockito.when(resultSet.getBytes("image")).thenReturn(new byte[0]);
        Mockito.when(resultSet.getString("name")).thenReturn("MEDICATION_NAME");
        Mockito.when(resultSet.getInt("id")).thenReturn(1);

        // Create an instance of the RowMapper
        RowMapper<Medication> rowMapper = new MedicationRowMapper();

        // Call the mapRow method
        Medication medication = rowMapper.mapRow(resultSet, 1);

        // Verify the mapped values
        Assertions.assertEquals("MEDICATION_CODE", medication.getCode());
        Assertions.assertEquals(5, medication.getWeight());
        Assertions.assertEquals(1, medication.getDroneId());
        Assertions.assertEquals(1, medication.getId());
        Assertions.assertArrayEquals(new byte[0], medication.getImage());
        Assertions.assertEquals("MEDICATION_NAME", medication.getName());
    }
}