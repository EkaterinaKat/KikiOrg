package com.katyshevtseva.kikiorg.core.sections.diary.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class IndMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "indicator_id")
    private Indicator indicator;

    @ManyToOne
    @JoinColumn(name = "date_entity_id")
    private DateEntity dateEntity;

    @ManyToOne
    @JoinColumn(name = "value_id")
    private IndValue value;

    private String comment;
}
