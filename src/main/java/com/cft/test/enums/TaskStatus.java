package com.cft.test.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TaskStatus {
    @JsonProperty("NEW")
    NEW(1, "New"),
    @JsonProperty("IN_PROGRESS")
    IN_PROGRESS(2, "In Progress"),
    @JsonProperty("CLOSED")
    CLOSED(3, "Closed");

    private int index;
    private String value;

    TaskStatus(int index, String value) {
        this.index = index;
        this.value = value;
    }

    @JsonCreator
    public static TaskStatus fromValue(String value) {
        for (TaskStatus status : TaskStatus.values()) {
            if (String.valueOf(status.value).equals(value)) {
                return status;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.value;
    }
}
