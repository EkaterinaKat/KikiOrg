package com.katyshevtseva.kikiorg.core.sections.study.entity;

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

    @ManyToOne
    @JoinColumn(name = "value_id")
    private SubjValue value;

    private String comment;

    public SubjMark(Subject subject, DateEntity dateEntity, SubjValue value, String comment) {
        this.subject = subject;
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
    public SubjMark getMark() {
        return this;
    }
}
