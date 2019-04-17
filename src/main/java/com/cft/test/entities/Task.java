package com.cft.test.entities;

import com.cft.test.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Project project;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private short priority;

    @Column
    private ZonedDateTime dateCreated;

    @Column
    private ZonedDateTime dateLastModified;

    @Column
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
}
