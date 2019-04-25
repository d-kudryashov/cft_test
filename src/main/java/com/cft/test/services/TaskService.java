package com.cft.test.services;

import com.cft.test.dtos.TaskDTO;
import com.cft.test.entities.Project;
import com.cft.test.entities.Task;
import com.cft.test.enums.TaskStatus;
import com.cft.test.exceptions.EntityValidationException;
import com.cft.test.repositories.ProjectRepository;
import com.cft.test.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Task saveTask(TaskDTO taskDTO) throws EntityValidationException {
        if (Objects.nonNull(taskDTO.getId()) && taskRepository.existsById(taskDTO.getId()) || Objects.isNull(taskDTO.getId())) {
            Task taskToDB = validateTask(taskDTO);
            return taskRepository.save(taskToDB);
        }
        throw new EntityValidationException();
    }

    private Task validateTask(TaskDTO taskDTO) throws EntityValidationException {
        Optional<Task> storedTask = taskRepository.findById(taskDTO.getId());
        ZonedDateTime currentTime = ZonedDateTime.now();
        taskDTO.setDateLastModified(currentTime);
        if (taskDTO.getPriority() < 0) {
            throw new EntityValidationException();
        }
        if (storedTask.isPresent()) {
            Task taskFromDB = storedTask.get();
            if (taskFromDB.getStatus().equals(TaskStatus.CLOSED)) {
                throw new EntityValidationException();
            }
            taskDTO.setDateCreated(taskFromDB.getDateCreated());
            return new Task(taskDTO, taskFromDB.getProject());
        } else {
            taskDTO.setStatus(TaskStatus.NEW);
            taskDTO.setDateCreated(currentTime);
            Optional<Project> storedProject = projectRepository.findById(taskDTO.getProjectId());
            if (storedProject.isPresent()) {
                return new Task(taskDTO, storedProject.get());
            } else {
                throw new EntityValidationException();
            }
        }
    }

    public Page<TaskDTO> getTasksByProjectId(Pageable pageRequest, int projectId) {
        Page<Task> taskPage = taskRepository.findAllByProjectId(projectId, pageRequest);
        return new PageImpl<>(taskPage.getContent().stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList()), pageRequest, taskPage.getTotalElements());
    }

    public Page<TaskDTO> getTasks(Pageable pageRequest) {
        Page<Task> taskPage = taskRepository.findAll(pageRequest);
        return new PageImpl<>(taskPage.getContent().stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList()), pageRequest, taskPage.getTotalElements());
    }
}
