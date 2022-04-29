package com.katyshevtseva.kikiorg.core.sections.structure.enums;

public enum Sphere {
    MAINTENANCE("#b04f17"),
    HOBBY("#db4fa3"),
    LIFE_IMPROVEMENT("#19b017"),
    WORK("#6317b0");

    private final String color;

    Sphere(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
