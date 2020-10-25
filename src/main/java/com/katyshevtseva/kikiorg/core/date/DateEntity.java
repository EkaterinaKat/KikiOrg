package com.katyshevtseva.kikiorg.core.date;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.DATE)
    private Date value;
}
