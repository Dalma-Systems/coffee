package com.dalma.common.workorder.enums;

public enum WorkOrderDestination {
    POSITION_ZERO {
        @Override
        public WorkOrderDestination nextState() {
            return WAREHOUSE;
        }
    },
    WAREHOUSE {
        @Override
        public WorkOrderDestination nextState() {
            return WORK_STATION;
        }
    },
    WORK_STATION {
        @Override
        public WorkOrderDestination nextState() {
            return POSITION_ZERO;
        }
    },
    ;

    public abstract WorkOrderDestination nextState();
}
