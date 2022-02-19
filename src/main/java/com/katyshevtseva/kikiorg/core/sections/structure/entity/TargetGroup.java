package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import com.katyshevtseva.history.HasHistory;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(schema = "structure")
public class TargetGroup implements HasHistory<TargetGroupChangeAction> {
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

    @OneToMany(mappedBy = "targetGroup", fetch = FetchType.EAGER)
    private Set<TargetGroupChangeAction> history;

    @Override
    public List<TargetGroupChangeAction> getHistory() {
        return new ArrayList<>(history);
    }
}
