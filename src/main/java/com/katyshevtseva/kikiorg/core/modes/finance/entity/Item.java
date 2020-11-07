package com.katyshevtseva.kikiorg.core.modes.finance.entity;

import com.katyshevtseva.kikiorg.core.modes.finance.Owner;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    @Override
    public String toString() {
        return title;
    }

    @Enumerated(EnumType.STRING)
    private Owner owner;
}
