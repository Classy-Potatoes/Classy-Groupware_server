package com.potatoes.cg.project.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProjectStatusType {

    USABLE("usable"),
    DISABLE("disable"),
    DELETED("deleted");

    private final String value;

    ProjectStatusType(String value) { this.value = value; }

    @JsonCreator
    public static ProjectStatusType from(String value) {
        for(ProjectStatusType status : ProjectStatusType.values()){
            if(status.getValue().equals(value)){
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() { return value; }
}
