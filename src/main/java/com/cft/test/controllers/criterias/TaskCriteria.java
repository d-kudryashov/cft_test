package com.cft.test.controllers.criterias;

import com.cft.test.enums.TaskStatus;
import com.cft.test.exceptions.RequestValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCriteria {

    private int page;
    private int size;
    private ZonedDateTime lastModifiedFrom;
    private ZonedDateTime lastModifiedTo;
    private TaskStatus taskStatus;
    private Short priorityFrom;
    private Short priorityTo;

    public void validate() throws RequestValidationException {
        if (size > 20 || size < 0) {
            size = 20;
        }
        if (priorityFrom < 0) {
            priorityFrom = 0;
        }
        if (priorityFrom.compareTo(priorityTo) > 0) {
            throw new RequestValidationException();
        }
        if (lastModifiedFrom.isAfter(lastModifiedTo)) {
            throw new RequestValidationException();
        }
    }
}
