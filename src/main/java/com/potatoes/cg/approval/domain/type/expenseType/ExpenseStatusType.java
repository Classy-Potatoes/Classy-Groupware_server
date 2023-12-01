package com.potatoes.cg.approval.domain.type.expenseType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.potatoes.cg.approval.domain.type.approvalType.DocumentType;

public enum ExpenseStatusType {

    PERSONAL("personal"),

    CORPORATE("corporate");



    private final String value;

    ExpenseStatusType(String value) {
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

