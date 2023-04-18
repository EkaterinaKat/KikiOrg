package com.katyshevtseva.kikiorg.core.sections.dtt.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class DttLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "date_id")
    private DateEntity date;

    private String content;
}
