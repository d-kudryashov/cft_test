package com.cft.test.controllers;

import com.cft.test.entities.Project;
import com.cft.test.entities.Task;
import com.cft.test.repositories.ProjectRepository;
import com.cft.test.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = new ArrayList<>();
        projectRepository.findAll().forEach(projects::add);
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
    public ResponseEntity<Project> getProject(@PathVariable int id) {
        return ResponseEntity
                .of(projectRepository.findById(id));
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<Task>> getTasksByProjectId(@PathVariable int id) {
        List<Task> tasks = taskRepository.findAllByProjectId(id);
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
    public ResponseEntity<Project> saveProject(@RequestBody Project project) {
        if (Objects.nonNull(project)) {
            if (Objects.nonNull(project.getId()) && projectRepository.existsById(project.getId()) || Objects.isNull(project.getId())) {
                Project result = projectRepository.save(project);
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(result);
            }
            if (Objects.nonNull(project.getId()) && !projectRepository.existsById(project.getId())) {
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