package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.sections.tracker.ColorEntity;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;

import java.sql.Date;

public class OrgUtils {

    public static Period getPeriodByDp(DatePicker startDp, DatePicker endDp) {
        return new Period(Date.valueOf(startDp.getValue()), Date.valueOf(endDp.getValue()));
    }

    public static ColorEntity getColorEntity(Color color) {
        return new ColorEntity(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static String getColorString(ColorEntity color) {
        return " rgb(" + color.getRed() * 100 + "%, " + color.getGreen() * 100 + "%, " + color.getBlue() * 100 + "%) ";
    }
}