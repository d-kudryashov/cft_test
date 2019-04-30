package com.cft.test.services;

import com.cft.test.controllers.criterias.TaskCriteria;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cft.test.repositories.specifications.TaskSpecification.*;

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
        if (taskRepository.existsById(taskDTO.getId()) || taskDTO.getId() == 0) {
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

    public Page<TaskDTO> getTasksByProjectId(TaskCriteria taskCriteria, int projectId) {
        Pageable pageRequest = PageRequest.of(taskCriteria.getPage(), taskCriteria.getSize());
        Specification<Task> specification = generateSpecification(taskCriteria);

        specification = specification.and(projectIdEqualsTo(projectId));

        Page<Task> taskPage = taskRepository.findAll(specification, pageRequest);
        return new PageImpl<>(taskPage.getContent().stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList()), pageRequest, taskPage.getTotalElements());
    }

    public Optional<TaskDTO> getTaskById(int id) {
        return taskRepository
                .findById(id)
                .map(TaskDTO::new);
    }

    public boolean deleteTaskById(int id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<TaskDTO> getTasks(TaskCriteria taskCriteria) {
        Pageable pageRequest = PageRequest.of(taskCriteria.getPage(), taskCriteria.getSize());
        Specification<Task> specification = generateSpecification(taskCriteria);

        Page<Task> taskPage = taskRepository.findAll(specification, pageRequest);
        return new PageImpl<>(taskPage.getContent().stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList()), pageRequest, taskPage.getTotalElements());
    }

    private Specification<Task> generateSpecification(TaskCriteria taskCriteria) {
        Specification<Task> specification = Specification.not(null);

        if (Objects.nonNull(taskCriteria.getLastModifiedFrom())) {
            specification = specification.and(fromLastModifiedDate(taskCriteria.getLastModifiedFrom()));
        }
        if (Objects.nonNull(taskCriteria.getLastModifiedTo())) {
            specification = specification.and(tillLastModifiedDate(taskCriteria.getLastModifiedTo()));
        }
        if (Objects.nonNull(taskCriteria.getTaskStatus())) {
            specification = specification.and(taskStatusIs(taskCriteria.getTaskStatus()));
        }
        if (Objects.nonNull(taskCriteria.getPriorityFrom())) {
            specification = specification.and(priorityHigherThan(taskCriteria.getPriorityFrom()));
        }
        if (Objects.nonNull(taskCriteria.getPriorityTo())) {
            specification = specification.and(priorityLowerThan(taskCriteria.getPriorityTo()));
        }

        return specification;
    }
}
