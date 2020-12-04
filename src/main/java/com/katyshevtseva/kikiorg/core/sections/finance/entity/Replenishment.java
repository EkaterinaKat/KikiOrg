package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType.REPLENISHMENT;

@Data
@Entity
public class Replenishment implements Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private Long amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_id", nullable = false)
    private Source source;

    @ManyToOne
    @JoinColumn(name = "date_entity_id")
    private DateEntity dateEntity;

    @Override
    public Date getDate() {
        return dateEntity.getValue();
    }

    @Override
    public String getFromTitle() {
        return source.getTitle();
    }

    @Override
    public String getToTitle() {
        return account.getTitle();
    }

    @Override
    public String getDateString() {
        return dateEntity.getValue().toString();
    }

    @Override
    public FinanceOperationService.OperationType getType() {
        return REPLENISHMENT;
    }
}
