package com.katyshevtseva.kikiorg.core.sections.habits.entity;

public class ReportCell {
    private String text;
    private String color;

    private ReportCell(String text, String color) {
        this.text = text;
        this.color = color;
    }

    public enum Color {
        WHITE("#FFFFFF"), GREEN("#00FF00");
        private String code;

        Color(String code) {
            this.code = code;
        }
    }

    public static ReportCell empty() {
        return new ReportCell("", Color.WHITE.code);
    }

    public static ReportCell meta(String text) {
        return new ReportCell(text, Color.WHITE.code);
    }

    public static ReportCell filled(String text) {
        return new ReportCell(text, Color.GREEN.code);
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
