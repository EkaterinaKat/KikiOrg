package com.katyshevtseva.kikiorg.core.sections.diary.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Indicator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @OneToMany(mappedBy = "indicator", fetch = FetchType.EAGER)
    private List<IndValue> values;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Indicator indicator = (Indicator) o;

        return id == indicator.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Indicator{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
