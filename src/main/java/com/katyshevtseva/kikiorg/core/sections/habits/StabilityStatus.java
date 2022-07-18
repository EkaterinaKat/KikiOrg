package com.katyshevtseva.kikiorg.core.sections.habits;

public enum StabilityStatus {
    STABLE("#006400"), NOT_STABLE("#000000"), STABILITY_LOST("#800000");

    private final String color;

    StabilityStatus(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
