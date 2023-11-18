package com.katyshevtseva.kikiorg.core.sections.finance.entity;

import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService;
import com.katyshevtseva.kikiorg.core.sections.finance.Necessity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType.EXPENSE;

@Data
@Entity
public class PotentialExpense implements FinanceOperationService.Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private String comment;

    @Enumerated(EnumType.STRING)
    private Necessity necessity;

    @Override
    public Date getDate() {
        return null;
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
        return "";
    }

    @Override
    public String getToTitle() {
        return item.getTitle();
    }

    @Override
    public String getDateString() {
        return "";
    }

    @Override
    public String getAmountString() {
        return amount + "";
    }

    @Override
    public FinanceOperationService.OperationType getType() {
        return EXPENSE;
    }

    @Override
    public String getAdditionalInfo() {
        return "Potential " + necessity.toString() + (comment != null ? ("\n" + comment) : "");
    }
}
