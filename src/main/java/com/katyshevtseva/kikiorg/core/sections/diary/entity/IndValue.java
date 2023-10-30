package com.katyshevtseva.kikiorg.core.sections.diary.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class IndValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private String color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "indicator_id", nullable = false)
    private Indicator indicator;

    public int getComparingInt() {
        try {
            int i = Integer.parseInt(title);
            return i;
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public String getTitleAndDesc() {
        return title + (description != null ? "\n" + description : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndValue indValue = (IndValue) o;

        return id == indValue.id;
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
