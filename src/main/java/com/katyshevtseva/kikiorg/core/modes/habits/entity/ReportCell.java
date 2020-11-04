package com.katyshevtseva.kikiorg.core.modes.habits.entity;

public class ReportCell {
    private Type type;
    private String text;
    private String color;

    private ReportCell(Type type, String text, String color) {
        this.type = type;
        this.text = text;
        this.color = color;
    }

    public static ReportCell empty() {
        return new ReportCell(Type.EMPTY, "", "#FFFFFF");
    }

    public static ReportCell meta(String text) {
        return new ReportCell(Type.META, text, "#FFFFFF");
    }

    public static ReportCell filled(String text, String color) {
        return new ReportCell(Type.FILLED, text, color);
    }

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getColor() {
        return color;
    }

    enum Type {
        META, EMPTY, FILLED
    }
}
