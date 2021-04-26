package com.dalma.broker.contract;

public class Paths {

    public static final String BASE_PATH = "/api/broker"; // NOSONAR
    public static final String BY_ID = "/{id}";

    // Robot
    public static final String ROBOT = "/robot";
    public static final String NOTIFICATION = "/notification";
    public static final String STATUS = "/status";
    public static final String SUBSCRIPTION = "/subscription";

    // Warehouse
    public static final String WAREHOUSE = "/warehouse";
    public static final String MATERIAL = "/material";
    public static final String EXTERNAL = "/external";

    // Workstation
    public static final String STATION = "/station";
    public static final String WORK_STATION = "/work";
    public static final String IDLE_STATION = "/idle";

    // Work Order
    public static final String WORKORDER = "/workorder";
    public static final String FILTER = "/filter";
    public static final String INTEGRATE = "/integrate";

    private Paths() {
        // Utility class
    }
}
