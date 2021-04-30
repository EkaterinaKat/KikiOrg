package com.katyshevtseva.kikiorg.core.sections.habits.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import lombok.Data;

import javax.persistence.*;

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
}
