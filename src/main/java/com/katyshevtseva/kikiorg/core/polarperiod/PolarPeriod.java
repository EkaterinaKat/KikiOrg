package com.katyshevtseva.kikiorg.core.polarperiod;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class PolarPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "start_id")
    private DateEntity start;

    @Enumerated(EnumType.STRING)
    private TimeUnit timeUnit;

    private int period;

    @Override
    public String toString() {
        return period + " " + timeUnit + " from " + start.toString();
    }
}
