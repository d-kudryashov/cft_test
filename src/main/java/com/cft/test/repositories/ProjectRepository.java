package com.cft.test.repositories;

import com.cft.test.entities.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Integer> {

    Optional<Project> findDtoById(int id);
}
