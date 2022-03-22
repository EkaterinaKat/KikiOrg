package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import com.katyshevtseva.hierarchy.Group;
import com.katyshevtseva.history.HasHistory;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(schema = "structure")
public class TargetGroup implements HasHistory<TargetGroupChangeAction>, Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private TargetGroup parent;

    @Enumerated(EnumType.STRING)
    private TargetStatus status;

    @OrderColumn(name = "id")
    @OneToMany(mappedBy = "targetGroup", fetch = FetchType.EAGER)
    private List<TargetGroupChangeAction> history;

    public TargetGroup(String title, String description, TargetGroup parent, TargetStatus status) {
        this.title = title;
        this.description = description;
        this.parent = parent;
        this.status = status;
    }

    public TargetGroup() {
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
        TargetGroup that = (TargetGroup) o;
        return id == that.id;
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
