package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import com.katyshevtseva.kikiorg.core.sections.structure.ActivityStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private ActivityStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "activities_param_values",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "param_value_id"))
    private Set<ParamValue> paramValues;

    public Activity(String title, ActivityStatus status) {
        this.title = title;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        return id == activity.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
