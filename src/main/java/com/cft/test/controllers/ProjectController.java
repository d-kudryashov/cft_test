package com.cft.test.controllers;

import com.cft.test.controllers.criterias.ProjectCriteria;
import com.cft.test.controllers.criterias.TaskCriteria;
import com.cft.test.dtos.ProjectDTO;
import com.cft.test.dtos.TaskDTO;
import com.cft.test.exceptions.EntityValidationException;
import com.cft.test.exceptions.RequestValidationException;
import com.cft.test.repositories.ProjectRepository;
import com.cft.test.services.ProjectService;
import com.cft.test.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final TaskService taskService;

    @Autowired
    public ProjectController(ProjectRepository projectRepository,
                             ProjectService projectService,
                             TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @GetMapping(value = "/")
    public ResponseEntity<Page<ProjectDTO>> getProjects(ProjectCriteria projectCriteria) {
        try {
            projectCriteria.validate();
        } catch (RequestValidationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Page<ProjectDTO> projects = projectService.getProjects(projectCriteria);
        if (projects.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable int id) {
        return ResponseEntity
                .of(projectRepository.findById(id).map(ProjectDTO::new));
    }

    @GetMapping(value = "/{id}/tasks")
    public ResponseEntity<Page<TaskDTO>> getTasksByProjectId(@PathVariable int id, TaskCriteria taskCriteria) {
        try {
            taskCriteria.validate();
        } catch (RequestValidationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Page<TaskDTO> tasks = taskService.getTasksByProjectId(taskCriteria, id);
        if (tasks.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tasks);
    }

    @PutMapping("/")
    public ResponseEntity<ProjectDTO> saveProject(@RequestBody ProjectDTO projectDTO) {
        if (Objects.nonNull(projectDTO)) {
            try {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(new ProjectDTO(projectService.saveProject(projectDTO)));
            } catch (EntityValidationException e) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
        }
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProject(@PathVariable int id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }
}
