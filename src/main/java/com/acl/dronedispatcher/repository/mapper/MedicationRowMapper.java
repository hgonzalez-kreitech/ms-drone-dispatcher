package com.acl.dronedispatcher.repository.mapper;

import com.acl.dronedispatcher.domain.Medication;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class MedicationRowMapper implements RowMapper<Medication> {

    @Override
    public Medication mapRow(ResultSet rs, int rowNum) throws SQLException {
        Medication medication = new Medication();
        medication.setCode(rs.getString("code"));
        medication.setWeight(rs.getInt("weight"));
        medication.setDroneId(rs.getInt("drone_id"));
        medication.setImage(rs.getBytes("image"));
        medication.setName(rs.getString("name"));
        medication.setId(rs.getInt("id"));
        return medication;
    }
}
