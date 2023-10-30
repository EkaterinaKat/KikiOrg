package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.Necessity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType.EXPENSE;

@Data
@Entity
public class Expense implements Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private Long amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "date_entity_id")
    private DateEntity dateEntity;

    private String comment;

    @Enumerated(EnumType.STRING)
    private Necessity necessity;

    @Override
    public Date getDate() {
        return dateEntity.getValue();
    }

    @Override
    public long getGoneAmount() {
        return amount;
    }

    @Override
    public long getCameAmount() {
        return amount;
    }

    @Override
    public String getFromTitle() {
        return account.getTitleWithAdditionalInfo();
    }

    @Override
    public String getToTitle() {
        return item.getTitleWithAdditionalInfo();
    }

    @Override
    public String getDateString() {
        return dateEntity.getValue().toString();
    }

    @Override
    public FinanceOperationService.OperationType getType() {
        return EXPENSE;
    }

    @Override
    public String getAdditionalInfo() {

        return (necessity != null ? necessity.toString() : "") + (comment != null ? (" " + comment) : "");
    }

    @Override
    public String getAmountString() {
        return "" + amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return id == expense.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
