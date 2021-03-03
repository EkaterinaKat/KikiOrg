package com.katyshevtseva.kikiorg.core.sections.habits.entity;

import com.katyshevtseva.kikiorg.core.sections.habits.HabitGroup;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class HabitUnion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private HabitGroup habitGroup;

    @OneToMany(mappedBy = "habitUnion", fetch = FetchType.EAGER)
    List<Habit> habits;
}
