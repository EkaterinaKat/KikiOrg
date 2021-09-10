package com.katyshevtseva.kikiorg.core.report;

public class ReportCell {
    private final String text;
    private final String color;
    private final Type type;
    private final Integer width;

    private ReportCell(String text, String color, Type type, Integer width) {
        this.text = text;
        this.color = color;
        this.type = type;
        this.width = width;
    }

    public enum Color {
        WHITE("#FFFFFF"), GREEN("#00FF00"), SLATE_BLUE("#7B68EE");
        private final String code;

        Color(String code) {
            this.code = code;
        }
    }

    public static ReportCell empty() {
        return new ReportCell("", Color.WHITE.code, Type.EMPTY, null);
    }

    public static ReportCell date(String text) {
        return new ReportCell(text, Color.WHITE.code, Type.DATE, null);
    }

    public static ReportCell columnHead(String text) {
        return new ReportCell(text, Color.WHITE.code, Type.COLUMN_HEAD, null);
    }

    public static ReportCell filled(String text) {
        return new ReportCell(text, Color.GREEN.code, Type.FILLED, null);
    }

    public static ReportCell filled(String text, Color color) {
        return new ReportCell(text, color.code, Type.FILLED, null);
    }

    public static ReportCell filled(String text, Color color, int width) {
        return new ReportCell(text, color.code, Type.FILLED, width);
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

    public Integer getWidth() {
        return width;
    }

    enum Type {
        DATE, COLUMN_HEAD, EMPTY, FILLED
    }
}
