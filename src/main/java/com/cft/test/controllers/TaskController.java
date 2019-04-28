package com.cft.test.controllers;

import com.cft.test.controllers.criterias.TaskCriteria;
import com.cft.test.dtos.TaskDTO;
import com.cft.test.exceptions.EntityValidationException;
import com.cft.test.exceptions.RequestValidationException;
import com.cft.test.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "/")
    public ResponseEntity<Page<TaskDTO>> getTasks(TaskCriteria taskCriteria) {
        try {
            taskCriteria.validate();
        } catch (RequestValidationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Page<TaskDTO> tasks = taskService.getTasks(taskCriteria);
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
                .of(taskService.getTaskById(id));
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
                        .build();
            }
        }
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@PathVariable int id) {
        if (taskService.deleteTaskById(id)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }
}
