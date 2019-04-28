package com.cft.test.repositories.specifications;

import com.cft.test.entities.Project;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public class ProjectSpecification {

    private ProjectSpecification() {
    }

    public static Specification<Project> fromLastModifiedDate(final ZonedDateTime lastModifiedFrom) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dateLastModified"), lastModifiedFrom);
    }

    public static Specification<Project> tillLastModifiedDate(final ZonedDateTime lastModifiedTo) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("dateLastModified"), lastModifiedTo);
    }
}
