package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class ParamValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "param_id", nullable = false)
    private Param param;

    public ParamValue(String title, Param param) {
        this.title = title;
        this.param = param;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParamValue that = (ParamValue) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "ParamValue{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
