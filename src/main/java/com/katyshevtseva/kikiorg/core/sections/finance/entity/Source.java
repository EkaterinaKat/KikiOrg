package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.kikiorg.core.sections.finance.FinanceSearchService.OperationEnd;
import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService.Owner;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Source implements OperationEnd {
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
