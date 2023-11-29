package com.potatoes.cg.approval.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum DocumentType {

        LETTER("품의서"),
        EXPENSE("지출결의서"),
        VACATION("휴가신청서");


    private final String value;

    DocumentType(String value) {
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




