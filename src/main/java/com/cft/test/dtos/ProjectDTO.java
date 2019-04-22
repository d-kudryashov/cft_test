package com.cft.test.dtos;

import com.cft.test.entities.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

    private Integer id;
    private String name;
    private String description;
    private ZonedDateTime dateCreated;
    private ZonedDateTime dateLastModified;
    private List<TaskDTO> taskDTOList;

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.dateCreated = project.getDateCreated();
        this.dateLastModified = project.getDateLastModified();
        this.taskDTOList = project.getTasks()
                                    .stream()
                                    .map(TaskDTO::new)
                                    .collect(Collectors.toList());
    }
}
