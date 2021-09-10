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
        return new ReportCell("", Color.WHITE.code, Type.REGULAR, null);
    }

    public static ReportCell columnHead(String text) {
        return new ReportCell(text, Color.WHITE.code, Type.HEAD_COLUMN, null);
    }

    public static ReportCell filled(String text) {
        return new ReportCell(text, Color.WHITE.code, Type.REGULAR, null);
    }

    public static ReportCell filled(String text, Color color) {
        return new ReportCell(text, color.code, Type.REGULAR, null);
    }

    public static ReportCell filled(String text, Color color, int width) {
        return new ReportCell(text, color.code, Type.REGULAR, width);
    }

    public String getText() {
        return text;
    }

    public String getColor() {
        return color;
    }

    public boolean isColumnHead() {
        return type == Type.HEAD_COLUMN;
    }

    public Integer getWidth() {
        return width;
    }

    enum Type {
        HEAD_COLUMN, REGULAR
    }
}
