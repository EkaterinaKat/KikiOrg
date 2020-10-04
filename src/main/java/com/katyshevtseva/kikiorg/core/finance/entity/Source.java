package com.katyshevtseva.kikiorg.core.finance.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Source {
    @Id
    private long id;

    private String title;

    private String description;
}
