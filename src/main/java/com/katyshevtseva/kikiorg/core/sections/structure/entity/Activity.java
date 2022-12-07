package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "activities_param_values",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "param_value_id"))
    private List<ParamValue> paramValues;

    public Activity(String title) {
        this.title = title;
    }
}
