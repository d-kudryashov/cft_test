package com.cft.test.services;

import com.cft.test.entities.Task;
import com.cft.test.enums.TaskStatus;
import com.cft.test.exceptions.EntityValidationException;
import com.cft.test.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task saveTask(Task task) throws EntityValidationException {
        if (Objects.nonNull(task.getId()) && taskRepository.existsById(task.getId()) || Objects.isNull(task.getId())) {
            validateTask(task);
            return taskRepository.save(task);
        }
        throw new EntityValidationException();
    }

    private void validateTask(Task task) throws EntityValidationException {
        Optional<Task> storedTask = taskRepository.findById(task.getId());
        ZonedDateTime currentTime = ZonedDateTime.now();
        if (storedTask.isPresent()) {
            Task taskFromDB = storedTask.get();
            if (taskFromDB.getStatus().equals(TaskStatus.CLOSED)) {
                throw new EntityValidationException();
            }
            task.setDateCreated(taskFromDB.getDateCreated());
        } else {
            task.setStatus(TaskStatus.NEW);
            task.setDateCreated(currentTime);
        }
        task.setDateLastModified(currentTime);
        if (task.getPriority() < 0) {
            throw new EntityValidationException();
        }
    }
}
