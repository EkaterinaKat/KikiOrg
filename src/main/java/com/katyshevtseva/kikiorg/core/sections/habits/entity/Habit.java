package com.katyshevtseva.kikiorg.core.sections.habits.entity;

import com.katyshevtseva.kikiorg.core.sections.habits.HabitGroup;
import com.katyshevtseva.kikiorg.core.sections.habits.StabilityStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private HabitGroup habitGroup;

    private boolean active;

    @OneToOne
    @JoinColumn(name = "current_desc_id")
    private Description currentDescription;

    @Enumerated(EnumType.STRING)
    private StabilityStatus stabilityStatus;

    public String getTitleWithActiveInfo() {
        return String.format("%s (%s)", title, active ? "active" : "inactive");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Habit habit = (Habit) o;
        return id == habit.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return title;
    }
}
