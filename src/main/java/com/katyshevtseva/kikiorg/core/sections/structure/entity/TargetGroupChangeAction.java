package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import com.katyshevtseva.history.Action;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(schema = "structure")
public class TargetGroupChangeAction implements Action<TargetGroup> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "date_entity_id")
    private DateEntity dateEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
    private TargetGroup targetGroup;

    @Override
    public Date getDate() {
        return dateEntity.getValue();
    }

    @Override
    public void setEntity(TargetGroup targetGroup) {
        this.setTargetGroup(targetGroup);
    }

    @Override
    public long getOrder() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TargetGroupChangeAction that = (TargetGroupChangeAction) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
