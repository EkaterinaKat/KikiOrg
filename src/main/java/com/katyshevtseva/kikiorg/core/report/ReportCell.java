package com.katyshevtseva.kikiorg.core.report;

import com.katyshevtseva.fx.Styler.StandardColor;

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

    public static ReportCell empty() {
        return new ReportCell("", StandardColor.WHITE.getCode(), Type.REGULAR, null);
    }

    public static ReportCell columnHead(String text) {
        return new ReportCell(text, StandardColor.WHITE.getCode(), Type.HEAD_COLUMN, null);
    }

    public static ReportCell filled(String text) {
        return new ReportCell(text, StandardColor.WHITE.getCode(), Type.REGULAR, null);
    }

    public static ReportCell filled(String text, StandardColor color) {
        return new ReportCell(text, color.getCode(), Type.REGULAR, null);
    }

    public static ReportCell filled(String text, StandardColor color, int width) {
        return new ReportCell(text, color.getCode(), Type.REGULAR, width);
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
