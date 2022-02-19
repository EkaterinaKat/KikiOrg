package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import com.katyshevtseva.history.Action;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(schema = "structure")
public class TargetChangeAction implements Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "date_entity_id")
    private DateEntity dateEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private Target target;

    @Override
    public Date getDate() {
        return dateEntity.getValue();
    }
}
