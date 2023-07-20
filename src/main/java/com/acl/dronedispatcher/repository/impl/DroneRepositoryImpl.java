package com.acl.dronedispatcher.repository.impl;

import com.acl.dronedispatcher.domain.Drone;
import com.acl.dronedispatcher.repository.DroneRepository;
import com.acl.dronedispatcher.repository.mapper.DroneRowMapper;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DroneRepositoryImpl implements DroneRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public Optional<Drone> createDrone(Drone drone) {
        try {
            log.info("DroneRepositoryImpl.createDrone : {}", drone.toString());
            var query = "INSERT INTO drones (serial_number, model, weight, battery_capacity, state)"
                    + "VALUES (?, ?, ?, ?, ?)";

            try (var con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
                // Create a PreparedStatementCreator to hold the SQL statement and parameters
                PreparedStatementCreator psc = conn -> {
                    PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, drone.getSerialNumber());
                    ps.setString(2, drone.getModel());
                    ps.setInt(3, drone.getWeight());
                    ps.setInt(4, drone.getBatteryCapacity());
                    ps.setString(5, drone.getState());
                    return ps;
                };
                // Create a KeyHolder to store the generated keys
                KeyHolder keyHolder = new GeneratedKeyHolder();

                // Execute the update and retrieve the generated key
                jdbcTemplate.update(psc, keyHolder);
                // Retrieve the generated key
                keyHolder.getKeyList()
                        .forEach(mapKeys -> drone.setId((Integer) mapKeys.get("id")));
                return Optional.of(drone);
            }
        } catch (DataAccessException | SQLException e) {
            log.error("Error creating drone in database {}", e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Optional<Drone> updateDrone(Drone drone) {
        try {
            log.info("DroneRepositoryImpl.updateDrone : {}", drone.toString());
            var query = "UPDATE drones SET state = ?, weight = ? "
                    + "WHERE id = ?";
            try (var con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
                try (var ps = con.prepareStatement(query)) {
                    ps.setString(1, drone.getState());
                    ps.setInt(2, drone.getWeight());
                    ps.setInt(3, drone.getId());
                    ps.executeUpdate();
                    return Optional.of(drone);
                }
            }
        } catch (DataAccessException | SQLException e) {
            log.error("Error updating drone in the database {} ", e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Integer droneCount() {
        try {
            log.info("DroneRepositoryImpl.droneCount");
            var query = "SELECT COUNT(*) FROM drones";
            return jdbcTemplate.queryForObject(query, Integer.class);
        } catch (DataAccessException e) {
            log.error("Error counting drones in database {}", e.getLocalizedMessage());
            throw new ClientException(ErrorCode.DRONE_COUNT_FAILED);
        }
    }

    @Override
    public Optional<Drone> getDrone(Integer droneId) {
        try {
            log.info("DroneRepositoryImpl.getDrone: {}", droneId);
            var query = "SELECT id, serial_number, model, weight, battery_capacity, state FROM drones WHERE id = " + droneId + "";
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, new DroneRowMapper()));
        } catch (DataAccessException e) {
            log.error("Error getting drone in database {}", e.getLocalizedMessage());
            throw new ClientException(ErrorCode.DRONE_SERIAL_NUMBER_NOT_FOUND);
        }
    }

    @Override
    public Optional<List<Drone>> getDronesByStatesAndWeight(String states, Integer weight) {
        try {
            log.info("DroneRepositoryImpl.getDronesByState: {}", states);
            var query = "SELECT id, serial_number, model, weight, battery_capacity, state FROM drones WHERE state IN (" + states + ") AND weight < " + weight;
            return Optional.of(jdbcTemplate.query(query, new DroneRowMapper()));
        } catch (DataAccessException e) {
            log.error("Error getting drones in database {}", e.getLocalizedMessage());
            throw new ClientException(ErrorCode.DRONES_BY_STATE_QUERY_FAILED);
        }
    }

    @Override
    public Optional<List<Drone>> getAllDrones() {
        try {
            log.info("DroneRepositoryImpl.getAllDrones");
            var query = "SELECT id, serial_number, model, weight, battery_capacity, state FROM drones";
            return Optional.of(jdbcTemplate.query(query, new DroneRowMapper()));
        } catch (DataAccessException e) {
            log.error("Error getting drones in database {}", e.getLocalizedMessage());
            throw new ClientException(ErrorCode.ALL_DRONES_QUERY_FAILED);
        }
    }
}
