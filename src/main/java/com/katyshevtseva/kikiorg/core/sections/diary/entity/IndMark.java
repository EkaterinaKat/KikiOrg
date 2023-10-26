package com.katyshevtseva.kikiorg.core.sections.diary.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
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

    public IndMark(Indicator indicator, DateEntity dateEntity) {
        this.indicator = indicator;
        this.dateEntity = dateEntity;
    }

    public String getValueAndComment() {
        String valuePart = value != null ? value.getTitle() + "\n" : "";
        return valuePart + comment;
    }
}
