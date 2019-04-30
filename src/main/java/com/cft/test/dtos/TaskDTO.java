package com.cft.test.dtos;

import com.cft.test.entities.Task;
import com.cft.test.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private int id;
    private Integer projectId;
    private String name;
    private String description;
    private Short priority;
    private ZonedDateTime dateCreated;
    private ZonedDateTime dateLastModified;
    private TaskStatus status;

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.projectId = Objects.nonNull(task.getProject()) ? task.getProject().getId() : null;
        this.name = task.getName();
        this.description = task.getDescription();
        this.priority = task.getPriority();
        this.dateCreated = task.getDateCreated();
        this.dateLastModified = task.getDateLastModified();
        this.status = task.getStatus();
    }
}
