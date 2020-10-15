package com.katyshevtseva.kikiorg.core.modes.habits;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String desc;

    @Enumerated(EnumType.STRING)
    private HabitType type;

    private boolean active;
}
