package com.katyshevtseva.kikiorg.core.sections.structure.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.PeriodType;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "structure")
public class PolarPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "start_id")
    private DateEntity start;

    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    private int period;

    @Override
    public String toString() {
        return period + " " + periodType + " from " + start.toString();
    }
}
