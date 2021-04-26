package com.dalma.contract.dto.work.order.schedule;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class WorkOrderSchedule {
    private String id;
    private Date date;
    private List<String> scheduleIds;
}
