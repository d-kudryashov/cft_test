package com.cft.test.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum TaskStatus {
    NEW(1, "New"),
    IN_PROGRESS(2, "In Progress"),
    CLOSED(3, "Closed");

    private int value;
    private String name;

    TaskStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonCreator
    public static TaskStatus fromValue(int value){
        for(TaskStatus status : TaskStatus.values()){
            if(status.getValue() == (value)){
                return status;
            }
        }
        throw new IllegalArgumentException();
    }
}
