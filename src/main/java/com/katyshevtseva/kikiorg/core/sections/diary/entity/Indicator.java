package com.katyshevtseva.kikiorg.core.sections.diary.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Entity
public class Indicator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private Boolean archived;

    @OneToMany(mappedBy = "indicator", fetch = FetchType.EAGER)
    private List<IndValue> values;

    public String getTitleAndArchivedInfo() {
        return title + (archived ? " (archived)" : "");
    }

    public List<IndValue> getSortedValues() {
        return values.stream().sorted(Comparator.comparing(IndValue::getComparingInt)).collect(Collectors.toList());
    }

    public Optional<IndValue> getDefaultValue() {
        return values.stream().filter(IndValue::isDefaultValue).findFirst();
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
        return title;
    }
}
