package com.katyshevtseva.kikiorg.core.sections.study.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "start_id")
    private DateEntity start;

    @ManyToOne
    @JoinColumn(name = "end_id")
    private DateEntity end;

    private Integer minDays;

    private Integer minHourADay;

    private boolean archived;

    public void setValues(Subject subject, DateEntity start, DateEntity end, Integer minDays, Integer minHourADay) {
        this.subject = subject;
        this.start = start;
        this.end = end;
        this.minDays = minDays;
        this.minHourADay = minHourADay;
    }
}
