package com.katyshevtseva.kikiorg.core.sections.habits.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class EnumElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private Habit habit;

    @Override
    public String toString() {
        return title;
    }
}
