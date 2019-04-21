package com.cft.test.services;

import com.cft.test.entities.Project;
import com.cft.test.exceptions.EntityValidationException;
import com.cft.test.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveProject(Project project) throws EntityValidationException {
        if (Objects.nonNull(project.getId()) && projectRepository.existsById(project.getId()) || Objects.isNull(project.getId())) {
            validateProject(project);
            return projectRepository.save(project);
        }
        throw new EntityValidationException();
    }

    private void validateProject(Project project) {
        Optional<Project> storedProject = projectRepository.findById(project.getId());
        ZonedDateTime currentTime = ZonedDateTime.now();
        if (storedProject.isPresent()) {
            Project projectFromDB = storedProject.get();
            project.setDateCreated(projectFromDB.getDateCreated());
            project.setDateLastModified(currentTime);
        } else {
            project.setDateCreated(currentTime);
        }
        project.setDateLastModified(currentTime);
    }
}
