package com.katyshevtseva.kikiorg.core.sections.habits.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class StabilityCriterion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "habit_id")
    private Habit habit;

    private int daysTotal;

    private int daysHabitDone;

    @Override
    public String toString() {
        return String.format("%d/%d", daysHabitDone, daysTotal);
    }
}
