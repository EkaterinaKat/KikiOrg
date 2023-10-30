package com.katyshevtseva.kikiorg.core.sections.diary.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
public class Indicator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private Integer indOrder;

    @OneToMany(mappedBy = "indicator", fetch = FetchType.EAGER)
    private List<IndValue> values;

    public List<IndValue> getSortedValues() {
        return values.stream().sorted(Comparator.comparing(IndValue::getComparingInt)).collect(Collectors.toList());
    }

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
