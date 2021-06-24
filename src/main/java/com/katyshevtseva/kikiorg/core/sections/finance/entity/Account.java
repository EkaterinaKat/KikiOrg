package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Account implements OperationEnd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long amount;

    private String title;

    private String description;

    @Override
    public String toString() {
        return title;
    }

    @Override
    public OperationEndType getType() {
        return OperationEndType.ACCOUNT;
    }
}
