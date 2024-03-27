package com.katyshevtseva.kikiorg.core.sections.study.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private Boolean archived;

    @OneToMany(mappedBy = "subject", fetch = FetchType.EAGER)
    private List<SubjValue> values;

    private Boolean hidden;

    public String getTitleAndArchivedInfo() {
        return title + (archived ? " (archived)" : hidden ? "(H)" : "");
    }

    public List<SubjValue> getSortedValues() {
        return values.stream().sorted(Comparator.comparing(SubjValue::getComparingInt)).collect(Collectors.toList());
    }

    public Optional<SubjValue> getDefaultValue() {
        return values.stream().filter(SubjValue::isDefaultValue).findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        return id == subject.id;
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
