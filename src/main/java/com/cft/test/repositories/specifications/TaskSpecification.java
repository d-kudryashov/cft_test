package com.cft.test.repositories.specifications;

import com.cft.test.entities.Task;
import com.cft.test.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public class TaskSpecification {

    private TaskSpecification() {
    }

    public static Specification<Task> fromLastModifiedDate(final ZonedDateTime lastModifiedFrom) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dateLastModified"), lastModifiedFrom);
    }

    public static Specification<Task> tillLastModifiedDate(final ZonedDateTime lastModifiedTo) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("dateLastModified"), lastModifiedTo);
    }

    public static Specification<Task> taskStatusIs(final TaskStatus taskStatus) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), taskStatus);
    }

    public static Specification<Task> priorityHigherThan(final Short priorityFrom) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("priority"), priorityFrom);
    }

    public static Specification<Task> priorityLowerThan(final Short priorityFrom) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("priority"), priorityFrom);
    }
}
