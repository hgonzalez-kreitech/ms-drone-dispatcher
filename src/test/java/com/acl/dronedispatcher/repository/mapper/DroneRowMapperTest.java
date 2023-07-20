package com.acl.dronedispatcher.repository.mapper;

import com.acl.dronedispatcher.domain.Drone;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.RowMapper;

class DroneRowMapperTest {

    @Test
    void testMapRow() throws SQLException {
        // Prepare the ResultSet mock
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getString("serial_number")).thenReturn("DRONE_SERIAL");
        Mockito.when(resultSet.getInt("battery_capacity")).thenReturn(90);
        Mockito.when(resultSet.getInt("weight")).thenReturn(10);
        Mockito.when(resultSet.getString("model")).thenReturn("DRONE_MODEL");
        Mockito.when(resultSet.getString("state")).thenReturn("IDLE");
        Mockito.when(resultSet.getInt("id")).thenReturn(1);

        // Create an instance of the RowMapper
        RowMapper<Drone> rowMapper = new DroneRowMapper();

        // Call the mapRow method
        Drone drone = rowMapper.mapRow(resultSet, 1);

        // Verify the mapped values
        Assertions.assertEquals("DRONE_SERIAL", drone.getSerialNumber());
        Assertions.assertEquals(90, drone.getBatteryCapacity());
        Assertions.assertEquals(10, drone.getWeight());
        Assertions.assertEquals("DRONE_MODEL", drone.getModel());
        Assertions.assertEquals("IDLE", drone.getState());
        Assertions.assertEquals(1, drone.getId());
    }
}