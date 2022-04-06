package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import com.katyshevtseva.history.Action;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_TIME_FORMAT;

@Data
@Entity
@Table(schema = "structure")
public class CourseChangeAction implements Action<CourseOfAction> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseOfAction courseOfAction;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Override
    public String getDateString() {
        return READABLE_DATE_TIME_FORMAT.format(timestamp);
    }

    @Override
    public void setEntity(CourseOfAction courseOfAction) {
        this.setCourseOfAction(courseOfAction);
    }

    @Override
    public long getOrder() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseChangeAction that = (CourseChangeAction) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
