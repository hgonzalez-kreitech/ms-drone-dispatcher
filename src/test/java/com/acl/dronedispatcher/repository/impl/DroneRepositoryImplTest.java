package com.acl.dronedispatcher.repository.impl;

import com.acl.dronedispatcher.domain.Drone;
import com.acl.dronedispatcher.repository.mapper.DroneRowMapper;
import com.acl.dronedispatcher.service.exception.ClientException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DroneRepositoryImplTest {

    private JdbcTemplate jdbcTemplate;
    private DroneRepositoryImpl droneRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        droneRepository = new DroneRepositoryImpl(jdbcTemplate);
    }

    @Test
    void testCreateDrone_Success() throws SQLException {
        // Mock the necessary objects
        Drone drone = new Drone();
        drone.setId(1);
        drone.setSerialNumber("123456");
        drone.setModel("Model X");
        drone.setWeight(100);
        drone.setBatteryCapacity(5000);
        drone.setState("Active");

        KeyHolder keyHolderMock = mock(GeneratedKeyHolder.class);
        Connection connection = mock(Connection.class);
        when(jdbcTemplate.getDataSource()).thenReturn(mock(javax.sql.DataSource.class));
        when(jdbcTemplate.getDataSource().getConnection()).thenReturn(connection);
        when(keyHolderMock.getKeyList()).thenReturn(
                List.of(Map.of("id", 1))
        );

        // Create the PreparedStatementCreator mock
        PreparedStatementCreator pscMock = mock(PreparedStatementCreator.class);
        when(pscMock.createPreparedStatement(any(Connection.class))).thenReturn(mock(PreparedStatement.class));

        // Mock the update method to pass the KeyHolder mock
        doAnswer(invocation -> {
            KeyHolder holderArg = invocation.getArgument(1);
            holderArg.getKeyList().addAll(keyHolderMock.getKeyList());
            return 1;
        }).when(jdbcTemplate).update(eq(pscMock), any(KeyHolder.class));

        // Call the createDrone method
        var createdDrone = droneRepository.createDrone(drone);

        // Verify the result
        assertTrue(createdDrone.isPresent());
        assertEquals(1, createdDrone.get().getId());
    }

    @Test
    void testCreateDrone_Error() throws SQLException {
        Drone drone = new Drone();
        drone.setId(1);
        drone.setSerialNumber("SERIAL");
        drone.setModel("MODEL");
        drone.setWeight(100);
        drone.setBatteryCapacity(90);
        drone.setState("STATE");

        when(jdbcTemplate.getDataSource()).thenReturn(mock(javax.sql.DataSource.class));
        when(jdbcTemplate.getDataSource().getConnection()).thenThrow(mock(SQLException.class));

        Optional<Drone> result = droneRepository.createDrone(drone);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testUpdateDrone_Success() throws SQLException {
        Drone drone = new Drone();
        drone.setId(1);
        drone.setSerialNumber("SERIAL");
        drone.setState("NEW_STATE");
        drone.setWeight(200);

        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(jdbcTemplate.getDataSource()).thenReturn(mock(javax.sql.DataSource.class));
        when(jdbcTemplate.getDataSource().getConnection()).thenReturn(connection);
        when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.doNothing().when(preparedStatement).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.doNothing().when(preparedStatement).setInt(Mockito.anyInt(), Mockito.anyInt());
        Mockito.doReturn(1).when(preparedStatement).executeUpdate();

        Optional<Drone> result = droneRepository.updateDrone(drone);

        assertTrue(result.isPresent());
        assertEquals(drone, result.get());
    }

    @Test
    void testUpdateDrone_Error() throws SQLException {
        Drone drone = new Drone();
        drone.setId(1);
        drone.setSerialNumber("SERIAL");
        drone.setState("NEW_STATE");
        drone.setWeight(200);

        when(jdbcTemplate.getDataSource()).thenReturn(mock(javax.sql.DataSource.class));
        when(jdbcTemplate.getDataSource().getConnection()).thenThrow(mock(SQLException.class));

        Optional<Drone> result = droneRepository.updateDrone(drone);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testDroneCount_Success() {
        int expectedCount = 5;
        when(jdbcTemplate.queryForObject(Mockito.anyString(), eq(Integer.class)))
                .thenReturn(expectedCount);

        Integer result = droneRepository.droneCount();

        assertEquals(expectedCount, result);

        Mockito.verify(jdbcTemplate).queryForObject(Mockito.anyString(), eq(Integer.class));
    }

    @Test
    void testDroneCount_Error() {
        when(jdbcTemplate.queryForObject(Mockito.anyString(), eq(Integer.class)))
                .thenThrow(mock(DataAccessException.class));

        Assertions.assertThrows(ClientException.class, () -> droneRepository.droneCount());

        Mockito.verify(jdbcTemplate).queryForObject(Mockito.anyString(), eq(Integer.class));
    }

    @Test
    void testGetDrone_Success() {
        Integer droneId = 1;
        Drone expectedDrone = new Drone();
        when(jdbcTemplate.queryForObject(Mockito.anyString(), any(DroneRowMapper.class)))
                .thenReturn(expectedDrone);

        Optional<Drone> result = droneRepository.getDrone(droneId);

        assertTrue(result.isPresent());
        assertEquals(expectedDrone, result.get());

        Mockito.verify(jdbcTemplate).queryForObject(Mockito.anyString(), any(DroneRowMapper.class));
    }

    @Test
    void testGetDrone_NotFound() {
        Integer droneId = 1;
        when(jdbcTemplate.queryForObject(Mockito.anyString(), any(DroneRowMapper.class)))
                .thenThrow(mock(DataAccessException.class));

        Assertions.assertThrows(ClientException.class, () -> droneRepository.getDrone(droneId));

        Mockito.verify(jdbcTemplate).queryForObject(Mockito.anyString(), any(DroneRowMapper.class));
    }

    @Test
    void testGetDronesByStatesAndWeight_Success() {
        String states = "'IDLE','LOADING'";
        Integer weight = 500;
        List<Drone> expectedDrones = Arrays.asList(new Drone(), new Drone());
        when(jdbcTemplate.query(Mockito.anyString(), any(DroneRowMapper.class)))
                .thenReturn(expectedDrones);

        Optional<List<Drone>> result = droneRepository.getDronesByStatesAndWeight(states, weight);

        assertTrue(result.isPresent());
        assertEquals(expectedDrones, result.get());

        Mockito.verify(jdbcTemplate).query(Mockito.anyString(), any(DroneRowMapper.class));
    }

    @Test
    void testGetDronesByStates_Error() {
        String states = "'IDLE','LOADING'";
        Integer weight = 500;
        when(jdbcTemplate.query(Mockito.anyString(), any(DroneRowMapper.class)))
                .thenThrow(mock(DataAccessException.class));

        Assertions.assertThrows(ClientException.class, () -> droneRepository.getDronesByStatesAndWeight(states, weight));

        Mockito.verify(jdbcTemplate).query(Mockito.anyString(), any(DroneRowMapper.class));
    }

    @Test
    void testGetAllDrones_Success() {
        List<Drone> expectedDrones = Arrays.asList(new Drone(), new Drone());
        when(jdbcTemplate.query(Mockito.anyString(), any(DroneRowMapper.class)))
                .thenReturn(expectedDrones);

        Optional<List<Drone>> result = droneRepository.getAllDrones();

        assertTrue(result.isPresent());
        assertEquals(expectedDrones, result.get());

        Mockito.verify(jdbcTemplate).query(Mockito.anyString(), any(DroneRowMapper.class));

    }

    @Test
    void testGetAllDrones_Error() {
        when(jdbcTemplate.query(Mockito.anyString(), any(DroneRowMapper.class)))
                .thenThrow(mock(DataAccessException.class));

        Assertions.assertThrows(ClientException.class, () -> droneRepository.getAllDrones());

        Mockito.verify(jdbcTemplate).query(Mockito.anyString(), any(DroneRowMapper.class));
    }
}