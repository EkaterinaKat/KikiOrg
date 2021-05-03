package com.katyshevtseva.kikiorg.core.sections.habits.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "beginning_date_id")
    private DateEntity beginningDate;

    @ManyToOne
    @JoinColumn(name = "end_date_id")
    private DateEntity endDate;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habit habit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Description that = (Description) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Description{" +
                "text='" + text + '\'' +
                ", beginningDate=" + beginningDate +
                ", endDate=" + endDate +
                '}';
    }
}
