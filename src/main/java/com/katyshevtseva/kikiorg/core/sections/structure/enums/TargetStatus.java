package com.katyshevtseva.kikiorg.core.sections.structure.enums;

public enum TargetStatus {
    NEW("#0e80c2"),
    STARTED("#c20e50"),
    REJECTED("#858585"),
    DONE("#45b500");

    private final String color;

    TargetStatus(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

}
