package com.potatoes.cg.approval.domain.type.approvalLineType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ApprovalLineResultType {

    APPROVE("승인"),
    TURNBACK("반려");

    private final String value;

    ApprovalLineResultType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static ApprovalLineResultType from(String value) {
        for (ApprovalLineResultType status : ApprovalLineResultType.values()) {
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
