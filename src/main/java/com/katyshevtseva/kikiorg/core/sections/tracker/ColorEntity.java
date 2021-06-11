package com.katyshevtseva.kikiorg.core.sections.tracker;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class ColorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double red;
    private double green;
    private double blue;

    public ColorEntity(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}
