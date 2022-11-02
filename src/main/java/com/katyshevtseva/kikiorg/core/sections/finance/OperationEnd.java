package com.katyshevtseva.kikiorg.core.sections.finance;

public interface OperationEnd {
    long getId();

    String getTitleWithAdditionalInfo();

    String getTitle();

    String getDescription();

    OperationEndType getType();

    enum OperationEndType {
        ITEM, ACCOUNT, SOURCE
    }
}
