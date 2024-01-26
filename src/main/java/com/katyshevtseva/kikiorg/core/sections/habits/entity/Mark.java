package com.katyshevtseva.kikiorg.core.sections.habits.entity;

import com.katyshevtseva.general.HasDate;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Mark implements HasDate {
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
    public Date getDate() {
        return dateEntity.getValue();
    }
}
