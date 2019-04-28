package com.cft.test.controllers.criterias;

import com.cft.test.exceptions.RequestValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCriteria {

    private int page;
    private int size;
    private ZonedDateTime lastModifiedFrom;
    private ZonedDateTime lastModifiedTo;

    public void validate() throws RequestValidationException {
        if (size > 20 || size < 0) {
            size = 20;
        }
        if (lastModifiedFrom.isAfter(lastModifiedTo)) {
            throw new RequestValidationException();
        }
    }
}
