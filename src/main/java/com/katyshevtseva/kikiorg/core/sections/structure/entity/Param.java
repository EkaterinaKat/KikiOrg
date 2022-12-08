package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Param {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @OneToMany(mappedBy = "param", fetch = FetchType.EAGER)
    private Set<ParamValue> values;

    private boolean required;

    private boolean singleValue;

    public Param(String title, boolean required, boolean singleValue) {
        this.title = title;
        this.required = required;
        this.singleValue = singleValue;
    }

    public String getAdditionalInfo() {
        return "(" + (required ? "required" : "optional") + ", " + (singleValue ? "single value" : "multiple values") + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Param param = (Param) o;

        return id == param.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Param{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", values=" + values +
                '}';
    }
}
