package com.cft.test.controllers;

import com.cft.test.dtos.TaskDTO;
import com.cft.test.exceptions.EntityValidationException;
import com.cft.test.repositories.TaskRepository;
import com.cft.test.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskRepository taskRepository, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @GetMapping(value = "/", params = {"page", "size"})
    public ResponseEntity<List<TaskDTO>> getTasks(@RequestParam("page") int page, @RequestParam("size") int size) {
        List<TaskDTO> tasks = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page, size);
        taskRepository.findAll(pageRequest).forEach(task -> tasks.add(new TaskDTO(task)));
        if (tasks.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable int id) {
        return ResponseEntity
                .of(taskRepository.findById(id).map(TaskDTO::new));
    }

    @PutMapping("/")
    public ResponseEntity<TaskDTO> saveTask(TaskDTO taskDTO) {
        if (Objects.nonNull(taskDTO) && Objects.nonNull(taskDTO.getProjectId())) {
            try {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(new TaskDTO(taskService.saveTask(taskDTO)));
            } catch (EntityValidationException e) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build();            }
        }
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@PathVariable int id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }
}
