package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import com.katyshevtseva.hierarchy.Group;
import com.katyshevtseva.hierarchy.Leaf;
import com.katyshevtseva.history.HasHistory;
import com.katyshevtseva.kikiorg.core.polarperiod.PolarPeriod;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(schema = "structure")
public class Target implements HasHistory<TargetChangeAction>, Leaf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    @OneToOne
    @JoinColumn(name = "period_id")
    private PolarPeriod period;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private TargetGroup parent;

    @Enumerated(EnumType.STRING)
    private TargetStatus status;

    @OrderColumn(name = "id")
    @OneToMany(mappedBy = "target", fetch = FetchType.EAGER)
    private List<TargetChangeAction> history;

    public Target(String title, String description, TargetGroup parent, TargetStatus status) {
        this.title = title;
        this.description = description;
        this.parent = parent;
        this.status = status;
    }

    public Target() {
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public void setParentGroup(Group group) {
        setParent((TargetGroup) group);
    }

    @Override
    public Group getParentGroup() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Target target = (Target) o;
        return id == target.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String getConditionDescForHistory() {
        return "{title='" + title + "', description='" + description + "'}";
    }
}
