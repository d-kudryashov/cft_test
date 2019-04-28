package com.cft.test.repositories;

import com.cft.test.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Integer>, JpaSpecificationExecutor<Task> {

    Page<Task> findAllByProjectId(int projectId, Pageable pageable, Specification specification);
}
