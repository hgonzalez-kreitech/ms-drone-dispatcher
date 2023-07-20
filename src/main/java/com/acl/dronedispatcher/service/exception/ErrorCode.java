package com.acl.dronedispatcher.service.exception;

public enum ErrorCode {

    DRONE_SERIAL_NUMBER_NOT_FOUND("Drone serial number not found"),
    MEDICATION_CODE_NOT_FOUND("Medication code not found"),
    MEDICATION_OVERLOAD("Medication overload"),
    DRONE_FLEET_SIZE_EXCEEDED( "Drone fleet size exceeded"),
    DRONE_CREATION_FAILED( "Drone creation failed"),
    MEDICATION_CREATION_FAILED( "Medication creation failed"),
    DRONE_LOADED_MEDICATIONS_QUERY_FAILED( "Drone loaded medications query failed"),
    DRONES_BY_STATE_QUERY_FAILED( "Drones by state query failed"),
    ALL_DRONES_QUERY_FAILED( "All drones query failed"),
    DRONE_COUNT_FAILED( "Drone fetch count failed"),
    BATTERY_LEVEL_ERROR( "Drone cannot change state to LOADING with battery level under 25");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
