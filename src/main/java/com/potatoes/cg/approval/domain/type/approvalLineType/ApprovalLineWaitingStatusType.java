package com.potatoes.cg.approval.domain.type.approvalLineType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ApprovalLineWaitingStatusType {

    REQUEST("결재요청"),
    WAIT("결재대기"),
    COMPLETE ("결재완료");




    private final String value;

    ApprovalLineWaitingStatusType(String value) {
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
