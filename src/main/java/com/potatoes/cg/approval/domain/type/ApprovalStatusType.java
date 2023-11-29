package com.potatoes.cg.approval.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ApprovalStatusType {

    WAITING("결재대기"),
    PAYING("결재중"),
    APPROVE("승인"),
    TURNBACK("반려"),
    RECALL("회수");

    private final String value;

    ApprovalStatusType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static ApprovalStatusType from(String value) {
        for (ApprovalStatusType status : ApprovalStatusType.values()) {
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
