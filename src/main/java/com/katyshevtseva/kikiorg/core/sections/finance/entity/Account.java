package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.general.HasShortInfo;
import com.katyshevtseva.kikiorg.core.sections.finance.Currency;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Account implements OperationEnd, HasShortInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long amount;

    private String title;

    private String description;

    private boolean archived;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return id == account.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String getShortInfo() {
        return title + " " + ((char) currency.getCode());
    }
}
