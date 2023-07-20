package com.acl.dronedispatcher.repository.mapper;

import com.acl.dronedispatcher.domain.Drone;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class DroneRowMapper implements RowMapper<Drone> {

    @Override
    public Drone mapRow(ResultSet rs, int rowNum) throws SQLException {
        Drone drone = new Drone();
        drone.setSerialNumber(rs.getString("serial_number"));
        drone.setBatteryCapacity(rs.getInt("battery_capacity"));
        drone.setWeight(rs.getInt("weight"));
        drone.setModel(rs.getString("model"));
        drone.setState(rs.getString("state"));
        drone.setId(rs.getInt("id"));
        return drone;
    }
}
