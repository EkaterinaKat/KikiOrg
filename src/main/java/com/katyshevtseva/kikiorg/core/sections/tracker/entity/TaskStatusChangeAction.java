package com.katyshevtseva.kikiorg.core.sections.tracker.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.tracker.TaskStatus;
import lombok.Data;

import javax.persistence.*;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;

@Data
@Entity
public class TaskStatusChangeAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "date_entity_id")
    private DateEntity dateEntity;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    public TaskStatusChangeAction() {
    }

    public TaskStatusChangeAction(Task task, DateEntity dateEntity, TaskStatus status) {
        this.task = task;
        this.dateEntity = dateEntity;
        this.status = status;
    }

    @Override
    public String toString() {
        return status + ": " + READABLE_DATE_FORMAT.format(dateEntity.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskStatusChangeAction action = (TaskStatusChangeAction) o;

        return id == action.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
