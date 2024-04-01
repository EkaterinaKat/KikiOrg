package com.katyshevtseva.kikiorg.core.sections.study.entity;

import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.study.StudyTableService;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class SubjMark implements StudyTableService.MarkToEdit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "date_entity_id")
    private DateEntity dateEntity;

    private Integer minutes;

    private String comment;

    public SubjMark(Subject subject, DateEntity dateEntity, int minutes, String comment) {
        this.subject = subject;
        this.dateEntity = dateEntity;
        this.comment = comment;
        this.minutes = minutes;
    }

    public String getValueAndComment() {
        String commentPart = GeneralUtils.isEmpty(comment) ? "" : "\n" + comment;
        return minutes + commentPart;
    }

    @Override
    public Date getDate() {
        return dateEntity.getValue();
    }

    @Override
    public SubjMark getMark() {
        return this;
    }

    public Float getHours() {
        return minutes.floatValue() / 60f;
    }
}
