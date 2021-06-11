package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.fx.DesignInfo;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.kikiorg.core.sections.tracker.ColorEntity;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;

import java.sql.Date;

public class OrgUtils {

    private OrgUtils() {
    }

    public static Period getPeriodByDp(DatePicker startDp, DatePicker endDp) {
        return new Period(Date.valueOf(startDp.getValue()), Date.valueOf(endDp.getValue()));
    }

    public static String getCssPath() {
        return "/css/general_style.css";
    }

    static String getIcoImagePath() {
        return "/images/ico.png";
    }

    public static StandardDialogBuilder getDialogBuilder() {
        return new StandardDialogBuilder().setCssPath(getCssPath()).setIconPath(getIcoImagePath());
    }

    public static ComponentBuilder getComponentBuilder() {
        return new ComponentBuilder().setDesignInfo(getDesignInfo());
    }

    public static ColorEntity getColorEntity(Color color) {
        return new ColorEntity(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static String getColorString(ColorEntity color) {
        return " rgb(" + color.getRed() * 100 + "%, " + color.getGreen() * 100 + "%, " + color.getBlue() * 100 + "%) ";
    }

    private static DesignInfo getDesignInfo() {
        DesignInfo designInfo = new DesignInfo();
        designInfo.setCssPath(getCssPath());
        designInfo.setIconPath(getIcoImagePath());
        return designInfo;
    }
}