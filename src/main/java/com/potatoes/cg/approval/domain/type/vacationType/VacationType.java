package com.potatoes.cg.approval.domain.type.vacationType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;

public enum VacationType {
    ANNUAL_LEAVE("연차"),
    SICK_LEAVE("병가"),
    HALF_DAY_OFF("반차"),
    COMP_TIME("보상휴가"),
    ETC("기타");








    private final String value;

    VacationType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static DocumentType from(String value) {
        for (DocumentType status : DocumentType.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }

        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    }

