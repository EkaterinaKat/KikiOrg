package com.katyshevtseva.kikiorg.core.sections.tracker.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.tracker.TaskStatus;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private int number;

    @ManyToOne
    @JoinColumn(name = "creation_date_id")
    private DateEntity creationDate;

    @ManyToOne
    @JoinColumn(name = "completion_date_id")
    private DateEntity completionDate;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
}
