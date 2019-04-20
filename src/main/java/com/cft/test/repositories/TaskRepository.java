package com.cft.test.repositories;

import com.cft.test.entities.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {

    List<Task> findAllByProjectId(int projectId);
}
