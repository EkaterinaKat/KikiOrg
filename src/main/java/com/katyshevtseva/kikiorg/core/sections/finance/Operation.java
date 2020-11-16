package com.katyshevtseva.kikiorg.core.sections.finance;

import java.util.Date;

public class Operation {
    private final Long id;
    private final String from;
    private final String to;
    private final Date date;
    private final Long amount;
    private final OperationType type;

    public Operation(Long id, String from, String to, Date date, Long amount, OperationType type) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    enum OperationType {
        TRANSFER, EXPENSE, REPLENISHMENT
    }

    public Date getDateForSorting() {
        return date;
    }

    public Long getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDate() {
        return date.toString();
    }

    public String getAmount() {
        return "" + amount;
    }

    public OperationType getType() {
        return type;
    }
}
