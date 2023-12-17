package com.potatoes.cg.note.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NoteStatusType {

    DEFAULT("default"),
    IMPORTANT("important"),
    DELETED("deleted");

    private final String value;

    NoteStatusType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static NoteStatusType from(String value) {

        for (NoteStatusType status : NoteStatusType.values()) {
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