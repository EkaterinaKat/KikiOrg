package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType.TRANSFER;

@Data
@Entity
public class Transfer implements Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account from;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account to;

    private Long amount;

    @ManyToOne
    @JoinColumn(name = "date_entity_id")
    private DateEntity dateEntity;

    @Override
    public Date getDate() {
        return dateEntity.getValue();
    }

    @Override
    public String getFromTitle() {
        return from.toString();
    }

    @Override
    public String getToTitle() {
        return to.toString();
    }

    @Override
    public String getDateString() {
        return dateEntity.getValue().toString();
    }

    @Override
    public FinanceOperationService.OperationType getType() {
        return TRANSFER;
    }

    @Override
    public String getAmountString() {
        return "" + amount;
    }

    public boolean isOuter() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return id == transfer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
