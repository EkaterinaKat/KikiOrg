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
public class Target implements HasHistory<TargetChangeAction> {
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

    @OneToMany(mappedBy = "target", fetch = FetchType.EAGER)
    private Set<TargetChangeAction> history;

    @Override
    public List<TargetChangeAction> getHistory() {
        return new ArrayList<>(history);
    }
}
