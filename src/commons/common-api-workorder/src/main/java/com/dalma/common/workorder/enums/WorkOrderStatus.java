package com.dalma.common.workorder.enums;

import static com.dalma.common.workorder.enums.WorkOrderDestination.POSITION_ZERO;
import static com.dalma.common.workorder.enums.WorkOrderDestination.WAREHOUSE;
import static com.dalma.common.workorder.enums.WorkOrderDestination.WORK_STATION;

public enum WorkOrderStatus {
    SCHEDULED {
        @Override
        public WorkOrderStatus nextState() {
            return ASSIGNED;
        }

        @Override
        public WorkOrderDestination currentDestination() {
            return POSITION_ZERO;
        }

        @Override
        public WorkOrderDestination nextDestination() {
            return POSITION_ZERO;
        }
    },
    ASSIGNED {
        @Override
        public WorkOrderStatus nextState() {
            return STARTED;
        }

        @Override
        public WorkOrderDestination currentDestination() {
            return POSITION_ZERO;
        }

        @Override
        public WorkOrderDestination nextDestination() {
            return WAREHOUSE;
        }
    },
    STARTED {
        @Override
        public WorkOrderStatus nextState() {
            return LOADING_MATERIAL;
        }

        @Override
        public WorkOrderDestination currentDestination() {
            return WAREHOUSE;
        }

        @Override
        public WorkOrderDestination nextDestination() {
            return WAREHOUSE;
        }
    },
    LOADING_MATERIAL {
        @Override
        public WorkOrderStatus nextState() {
            return TRANSPORTING_MATERIAL;
        }

        @Override
        public WorkOrderDestination currentDestination() {
            return WAREHOUSE;
        }

        @Override
        public WorkOrderDestination nextDestination() {
            return WORK_STATION;
        }
    },
    TRANSPORTING_MATERIAL {
        @Override
        public WorkOrderStatus nextState() {
            return UNLOADING_MATERIAL;
        }

        @Override
        public WorkOrderDestination currentDestination() {
            return WORK_STATION;
        }

        @Override
        public WorkOrderDestination nextDestination() {
            return WORK_STATION;
        }
    },
    UNLOADING_MATERIAL {
        @Override
        public WorkOrderStatus nextState() {
            return ENDED;
        }

        @Override
        public WorkOrderDestination currentDestination() {
            return WORK_STATION;
        }

        @Override
        public WorkOrderDestination nextDestination() {
            return POSITION_ZERO;
        }
    },
    ENDED {
        @Override
        public WorkOrderStatus nextState() {
            return null;
        }

        @Override
        public WorkOrderDestination currentDestination() {
            return POSITION_ZERO;
        }

        @Override
        public WorkOrderDestination nextDestination() {
            return POSITION_ZERO;
        }
    },
    CANCELED {
        @Override
        public WorkOrderStatus nextState() {
            return null;
        }

        @Override
        public WorkOrderDestination currentDestination() {
            return null;
        }

        @Override
        public WorkOrderDestination nextDestination() {
            return POSITION_ZERO;
        }
    };

    public static final WorkOrderStatus INITIAL_STATE = WorkOrderStatus.SCHEDULED;

    private WorkOrderStatus() {
    }

    public String getStatus() {
        return this.name().toLowerCase();
    }

    public static WorkOrderStatus getWorkOrderStatus(String status) {
        if (status == null) {
            return null;
        }

        for (WorkOrderStatus orderStatus : values()) {
            if (orderStatus.name().equalsIgnoreCase(status)) {
                return orderStatus;
            }
        }

        return null;
    }

    public abstract WorkOrderStatus nextState();

    public abstract WorkOrderDestination nextDestination();

    public abstract WorkOrderDestination currentDestination();
}
