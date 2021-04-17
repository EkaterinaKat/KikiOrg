package com.katyshevtseva.kikiorg.core.report;

public class ReportCell {
    private String text;
    private String color;
    private Type type;

    private ReportCell(String text, String color, Type type) {
        this.text = text;
        this.color = color;
        this.type = type;
    }

    public enum Color {
        WHITE("#FFFFFF"), GREEN("#00FF00");
        private String code;

        Color(String code) {
            this.code = code;
        }
    }

    public static ReportCell empty() {
        return new ReportCell("", Color.WHITE.code, Type.EMPTY);
    }

    public static ReportCell date(String text) {
        return new ReportCell(text, Color.WHITE.code, Type.DATE);
    }

    public static ReportCell columnHead(String text) {
        return new ReportCell(text, Color.WHITE.code, Type.COLUMN_HEAD);
    }

    public static ReportCell filled(String text) {
        return new ReportCell(text, Color.GREEN.code, Type.FILLED);
    }

    public String getText() {
        return text;
    }

    public String getColor() {
        return color;
    }

    public boolean isColumnHead() {
        return type == Type.COLUMN_HEAD;
    }

    public boolean isDate() {
        return type == Type.DATE;
    }

    enum Type {
        DATE, COLUMN_HEAD, EMPTY, FILLED
    }
}
