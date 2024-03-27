package com.katyshevtseva.kikiorg.core.sections.study.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class SubjValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private String color;

    private boolean defaultValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    public int getComparingInt() {
        try {
            int i = Integer.parseInt(title);
            return i;
        } catch (NumberFormatException nfe) {
            return (int) id;
        }
    }

    public String getTitleAndDesc() {
        return title + (description != null ? "\n" + description : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubjValue subjValue = (SubjValue) o;

        return id == subjValue.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return getTitleAndDesc();
    }
}
