package com.katyshevtseva.kikiorg.core.sections.tracker.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Project implements Comparable<Project> {
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

    @Override
    public int compareTo(Project other) {
        return this.title.compareTo(other.getTitle());
    }
}