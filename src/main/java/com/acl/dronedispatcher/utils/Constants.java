package com.acl.dronedispatcher.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final int WEIGHT_LIMIT = 500;
    public static final int MIN_BATTERY_LEVEL = 25;
    public static final int DRONE_FLEET_LIMIT = 10;
}
