package com.katyshevtseva.kikiorg.core.sections.finance;

public enum Currency {
    RUB(8381), TL(8378), EUR(8364);

    private int code;

    Currency(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
