package com.katyshevtseva.kikiorg.core.sections.study.entity;

import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.study.StudyTableService;
import com.katyshevtseva.kikiorg.core.sections.study.enums.CircsType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Circs implements StudyTableService.CircsToEdit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "date_entity_id")
    private DateEntity dateEntity;

    @Enumerated(EnumType.STRING)
    private CircsType type;

    private String comment;

    public Circs(DateEntity dateEntity, CircsType type, String comment) {
        this.dateEntity = dateEntity;
        this.type = type;
        this.comment = comment;
    }

    public String getInfo() {
        return type.getTitle() + (GeneralUtils.isEmpty(comment) ? "" : "\n" + comment);
    }

    @Override
    public Date getDate() {
        return dateEntity.getValue();
    }

    @Override
    public Circs getCircs() {
        return this;
    }
}
