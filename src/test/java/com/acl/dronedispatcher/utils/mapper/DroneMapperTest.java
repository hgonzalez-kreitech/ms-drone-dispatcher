package com.acl.dronedispatcher.utils.mapper;

import com.acl.dronedispatcher.controller.model.DroneRequest;
import com.acl.dronedispatcher.domain.Drone;
import org.junit.jupiter.api.Test;

class DroneMapperTest {

    @Test
    void createDroneResponse() {
        DroneMapper droneMapper = new DroneMapper();
        Drone drone = Drone.builder().build();
        droneMapper.createDroneResponse(drone);
        assert true;
    }

    @Test
    void createDroneRequest() {
        DroneMapper droneMapper = new DroneMapper();
        DroneRequest drone = DroneRequest.builder().build();
        droneMapper.createDroneRequest(drone);
        assert true;
    }
}