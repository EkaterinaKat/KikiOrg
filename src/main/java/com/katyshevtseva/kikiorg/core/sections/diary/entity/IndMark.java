package com.katyshevtseva.kikiorg.core.sections.diary.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.diary.DairyTableService;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class IndMark implements DairyTableService.MarkToEdit {
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

    public IndMark(Indicator indicator, DateEntity dateEntity, IndValue value, String comment) {
        this.indicator = indicator;
        this.dateEntity = dateEntity;
        this.value = value;
        this.comment = comment;
    }

    public String getValueAndComment() {
        String valuePart = value != null ? value.getTitle() + "\n" : "";
        String commentPart = comment != null ? comment : "";
        return valuePart + commentPart;
    }

    @Override
    public Date getDate() {
        return dateEntity.getValue();
    }

    @Override
    public IndMark getMark() {
        return this;
    }
}
