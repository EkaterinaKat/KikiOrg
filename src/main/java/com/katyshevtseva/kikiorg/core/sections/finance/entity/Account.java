package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.kikiorg.core.sections.finance.Currency;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Account implements OperationEnd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long amount;

    private String title;

    private String description;

    private boolean archived;

    private Boolean my;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    public String getTitleWithAdditionalInfo() {
        String result = title + " " + ((char) currency.getCode());
        if (archived)
            return result + " (archived)";
        return result;
    }

    @Override
    public String toString() {
        return title + " " + ((char) currency.getCode());
    }

    @Override
    public OperationEndType getType() {
        return OperationEndType.ACCOUNT;
    }
}
