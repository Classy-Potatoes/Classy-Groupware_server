package com.potatoes.cg.approval.domain.type.approvalLineType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ApprovalLineTurnType {
    FIRST("first"),
    SECOND("second"),
    THIRD("third"),
    FOURTH("fourth"),
    FIFTH("fifth");

    private final String value;

    ApprovalLineTurnType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static ApprovalLineWaitingStatusType from(String value) {
        for (ApprovalLineWaitingStatusType status : ApprovalLineWaitingStatusType.values()) {
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

