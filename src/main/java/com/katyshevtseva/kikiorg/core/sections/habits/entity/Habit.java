package com.katyshevtseva.kikiorg.core.sections.habits.entity;

import com.katyshevtseva.history.HasHistory;
import com.katyshevtseva.kikiorg.core.sections.habits.StabilityStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
public class Habit implements HasHistory<HabitChangeAction> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private StabilityStatus stabilityStatus;

    @OneToMany(mappedBy = "habit", fetch = FetchType.EAGER)
    private Set<HabitChangeAction> history;

    private Integer criterionDaysTotal;

    private Integer criterionDaysDone;

    public String getCriterionString() {
        return String.format("%d/%d", criterionDaysDone, criterionDaysTotal);
    }

    public boolean hasCriterion() {
        return criterionDaysDone != null && criterionDaysTotal != null;
    }

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

    @Override
    public List<HabitChangeAction> getHistory() {
        return history.stream()
                .sorted(Comparator.comparing(habitChangeAction -> habitChangeAction.getDateEntity().getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public String getConditionDescForHistory() {
        return "{title='" + title + "', description='" + description + "'}";
    }
}
