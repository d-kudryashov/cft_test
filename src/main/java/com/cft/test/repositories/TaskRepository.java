package com.cft.test.repositories;

import com.cft.test.entities.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Integer> {

    List<Task> findAllByProjectId(int projectId, Pageable pageable);
}
