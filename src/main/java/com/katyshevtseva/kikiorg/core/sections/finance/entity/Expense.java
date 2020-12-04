package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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

    @Override
    public Date getDate() {
        return dateEntity.getValue();
    }

    @Override
    public String getFromTitle() {
        return account.getTitle();
    }

    @Override
    public String getToTitle() {
        return item.getTitle();
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
    public String getAmountString() {
        return "" + amount;
    }
}
