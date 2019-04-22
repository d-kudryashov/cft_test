package com.cft.test.controllers;

import com.cft.test.dtos.ProjectDTO;
import com.cft.test.dtos.TaskDTO;
import com.cft.test.exceptions.EntityValidationException;
import com.cft.test.repositories.ProjectRepository;
import com.cft.test.repositories.TaskRepository;
import com.cft.test.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository,
                             ProjectService projectService,
                             TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.projectService = projectService;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        List<ProjectDTO> projects = new ArrayList<>();
        projectRepository.findAll().forEach(project -> projects.add(new ProjectDTO(project)));
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
                .of(projectRepository.findDtoById(id).map(ProjectDTO::new));
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksByProjectId(@PathVariable int id) {
        List<TaskDTO> tasks = taskRepository.findAllByProjectId(id)
                                                        .stream()
                                                        .map(TaskDTO::new)
                                                        .collect(Collectors.toList());
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
