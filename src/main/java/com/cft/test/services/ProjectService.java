package com.cft.test.services;

import com.cft.test.controllers.criterias.ProjectCriteria;
import com.cft.test.dtos.ProjectDTO;
import com.cft.test.entities.Project;
import com.cft.test.exceptions.EntityValidationException;
import com.cft.test.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cft.test.repositories.specifications.ProjectSpecification.fromLastModifiedDate;
import static com.cft.test.repositories.specifications.ProjectSpecification.tillLastModifiedDate;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
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

    public Page<ProjectDTO> getProjects(ProjectCriteria projectCriteria) {
        Pageable pageRequest = PageRequest.of(projectCriteria.getPage(), projectCriteria.getSize());
        Specification<Project> specification = generateSpecifitaion(projectCriteria);

        Page<Project> projectPage = projectRepository.findAll(specification, pageRequest);
        return new PageImpl<>(projectPage.getContent().stream()
                .map(ProjectDTO::new)
                .collect(Collectors.toList()), pageRequest, projectPage.getTotalElements());
    }

    private Specification<Project> generateSpecifitaion(ProjectCriteria projectCriteria) {
        Specification<Project> specification = Specification.not(null);

        if (Objects.nonNull(projectCriteria.getLastModifiedFrom())) {
            specification.and(fromLastModifiedDate(projectCriteria.getLastModifiedFrom()));
        }
        if (Objects.nonNull(projectCriteria.getLastModifiedTo())) {
            specification.and(tillLastModifiedDate(projectCriteria.getLastModifiedTo()));
        }

        return specification;
    }
}
