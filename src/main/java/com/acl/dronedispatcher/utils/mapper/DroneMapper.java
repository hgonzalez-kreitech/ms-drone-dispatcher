package com.acl.dronedispatcher.utils.mapper;

import com.acl.dronedispatcher.controller.model.DroneRequest;
import com.acl.dronedispatcher.controller.model.DroneResponse;
import com.acl.dronedispatcher.domain.Drone;
import org.springframework.stereotype.Component;

@Component
public class DroneMapper {

  /**
   * Drone to DroneResponse.
   * @param drone
   * @return DroneResponse
   */
  public DroneResponse createDroneResponse(Drone drone) {
    return DroneResponse.builder()
            .id(drone.getId())
            .model(drone.getModel())
            .batteryCapacity(drone.getBatteryCapacity())
            .serialNumber(drone.getSerialNumber())
            .weight(drone.getWeight())
            .state(drone.getState())
            .build();
  }

  /**
   * DroneRequest to Drone.
   * @param drone
   * @return Drone
   */
  public Drone createDroneRequest(DroneRequest drone) {
    return Drone.builder()
            .model(drone.getModel())
            .batteryCapacity(drone.getBatteryCapacity())
            .serialNumber(drone.getSerialNumber())
            .weight(drone.getWeight())
            .state(drone.getState())
            .build();
  }
}
