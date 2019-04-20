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
    private Integer id;

    @ManyToOne(optional = false)
    private Project project;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private Short priority;

    @Column
    private ZonedDateTime dateCreated;

    @Column
    private ZonedDateTime dateLastModified;

    @Column
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
}
