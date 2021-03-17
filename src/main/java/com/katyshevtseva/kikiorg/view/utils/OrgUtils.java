package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.kikiorg.core.date.Period;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.IMAGE_LOCATION;

public class OrgUtils {

    private OrgUtils() {
    }

    public static Period getPeriodByDp(DatePicker startDp, DatePicker endDp) {
        return new Period(Date.valueOf(startDp.getValue()), Date.valueOf(endDp.getValue()));
    }

    public static String getGreenBackground() {
        return " -fx-background-color:#4FFF4C ";
    }

    public static String getOrangeBackground() {
        return " -fx-background-color:#FFA24C ";
    }

    public static String getBlueBackground() {
        return " -fx-background-color:#4C9FFF ";
    }

    public static String getPurpleTextStyle() {
        return " -fx-text-fill: #800080; ";
    }

    public static String getGreenTextStyle() {
        return " -fx-text-fill: #008000; ";
    }

    public static String getBlackTextStyle() {
        return " -fx-text-fill: #000000; ";
    }

    public static String getGrayTextStyle() {
        return " -fx-text-fill: #808080; ";
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
}