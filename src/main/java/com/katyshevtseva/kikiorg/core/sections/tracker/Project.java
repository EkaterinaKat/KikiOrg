package com.katyshevtseva.kikiorg.core.sections.tracker;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private String code;

    @ManyToOne
    @JoinColumn(name = "color_entity_id")
    private ColorEntity color;

    @Override
    public String toString() {
        return title;
    }
}
