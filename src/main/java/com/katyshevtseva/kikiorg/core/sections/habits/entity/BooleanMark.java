package com.katyshevtseva.kikiorg.core.sections.habits.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitMarkService.HabitMark;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class BooleanMark implements HabitMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habit habit;

    @ManyToOne
    @JoinColumn(name = "date_entity_id")
    private DateEntity dateEntity;

    @Override
    public String getTextForReport() {
        return "";
    }

    @Override
    public int getNumEquivalent() {
        return 1;
    }
}
