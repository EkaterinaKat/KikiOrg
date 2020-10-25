package com.katyshevtseva.kikiorg.core.modes.habits;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class EnumElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habit habit;

    @Override
    public String toString() {
        return title;
    }
}
