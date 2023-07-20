package com.acl.dronedispatcher.repository;

import com.acl.dronedispatcher.domain.Drone;
import java.util.List;
import java.util.Optional;

public interface DroneRepository {
    Optional<Drone> createDrone(Drone drone);
    Optional<Drone> updateDrone(Drone drone);
    Integer droneCount();
    Optional<Drone> getDrone(Integer droneId);
    Optional<List<Drone>> getDronesByStatesAndWeight(String states, Integer weight);
    Optional<List<Drone>> getAllDrones();
}
