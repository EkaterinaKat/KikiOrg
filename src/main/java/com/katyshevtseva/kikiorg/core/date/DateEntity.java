package com.katyshevtseva.kikiorg.core.date;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
public class DateEntity implements Comparable<DateEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.DATE)
    private Date value;

    @Override
    public int compareTo(DateEntity o) {
        return value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("dd.MM.yyyy").format(value);
    }

    public DateEntity() {
    }

    public DateEntity(Date value) {
        this.value = value;
    }
}
