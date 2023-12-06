package com.potatoes.cg.projectSchedule.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.potatoes.cg.calendar.domain.type.StatusType;

public enum ScheduleFileType {

    SCHEDULE("schedule");

    private final String value;

    ScheduleFileType(String value) { this.value = value; }

    @JsonCreator
    public static ScheduleFileType from(String value) {
        for(ScheduleFileType status : ScheduleFileType.values()) {
            if(status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() { return value; }

}
