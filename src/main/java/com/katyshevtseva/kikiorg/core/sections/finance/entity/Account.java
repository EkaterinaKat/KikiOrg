package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService.Owner;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long amount;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Owner owner;

    @Override
    public String toString() {
        return title;
    }

    public String getTitleWithOwnerInfo() {
        return title + " (owner: " + owner + ")";
    }
}
