package com.potatoes.cg.calendar.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusType {

    PROGRESS("progress"),
    DELETED("deleted"),
    PROJECT("project"),
    PERSONAL("personal"),
    FINISHED("finished"),
    UNFINISHED("unfinished");

    private final String value;

    StatusType(String value) { this.value = value; }

    @JsonCreator
    public static StatusType from(String value) {
        for(StatusType status : StatusType.values()) {
            if(status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() { return value; }
}
