package com.potatoes.cg.note.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NoteStatus {

    DEFAULT("default"),
    IMPORTANT("important"),
    DELETED("deleted");

    private final String value;

    NoteStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static NoteStatus from(String value) {

        for (NoteStatus status : NoteStatus.values()) {
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