package com.cft.test.services;

import com.cft.test.dtos.ProjectDTO;
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

    public Project saveProject(ProjectDTO projectDTO) throws EntityValidationException {
        if (Objects.nonNull(projectDTO.getId()) && projectRepository.existsById(projectDTO.getId()) || Objects.isNull(projectDTO.getId())) {
            validateProjectData(projectDTO);
            return projectRepository.save(new Project(projectDTO));
        }
        throw new EntityValidationException();
    }

    private void validateProjectData(ProjectDTO projectDTO) {
        Optional<Project> storedProject = projectRepository.findById(projectDTO.getId());
        ZonedDateTime currentTime = ZonedDateTime.now();
        if (storedProject.isPresent()) {
            Project projectFromDB = storedProject.get();
            projectDTO.setDateCreated(projectFromDB.getDateCreated());
            projectDTO.setDateLastModified(currentTime);
        } else {
            projectDTO.setDateCreated(currentTime);
        }
        projectDTO.setDateLastModified(currentTime);
    }
}
