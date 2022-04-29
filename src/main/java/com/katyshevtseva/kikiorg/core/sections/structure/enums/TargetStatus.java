package com.katyshevtseva.kikiorg.core.sections.structure.enums;

public enum TargetStatus {
    NEW("#0e80c2"),
    STARTED("#ff621f"),
    REJECTED("#858585"),
    DONE("#215203");

    private final String color;

    TargetStatus(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

}
