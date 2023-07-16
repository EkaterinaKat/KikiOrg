package com.katyshevtseva.kikiorg.core.sections.dtt.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;

@Data
@Entity
@NoArgsConstructor
public class DatelessTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private Boolean fake;

    @ManyToOne
    @JoinColumn(name = "sphere_id")
    private Sphere sphere;

    @ManyToOne
    @JoinColumn(name = "creation_date_id")
    private DateEntity creationDate;

    @ManyToOne
    @JoinColumn(name = "completion_date_id")
    private DateEntity completionDate;

    public Boolean getFake() {
        return fake != null && fake;
    }

    public String getDatesInfo() {
        String result = String.format("Creation: %s", READABLE_DATE_FORMAT.format(creationDate.getValue()));
        if (completionDate != null) {
            result += String.format("\nCompletion: %s", READABLE_DATE_FORMAT.format(completionDate.getValue()));
        }
        return result;
    }
}
