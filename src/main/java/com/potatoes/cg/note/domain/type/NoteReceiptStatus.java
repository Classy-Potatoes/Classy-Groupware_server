package com.potatoes.cg.note.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NoteReceiptStatus {

    UNREAD("unread"),
    READ("read");

    private final String value;

    NoteReceiptStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static NoteReceiptStatus from(String value) {

        for (NoteReceiptStatus status : NoteReceiptStatus.values()) {
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
