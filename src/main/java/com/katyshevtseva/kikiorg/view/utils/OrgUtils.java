package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import javafx.scene.control.DatePicker;

import java.sql.Date;

public class OrgUtils {

    public static Period getPeriodByDp(DatePicker startDp, DatePicker endDp) {
        return new Period(Date.valueOf(startDp.getValue()), Date.valueOf(endDp.getValue()));
    }

    public static void setDate(DatePicker datePicker, DateEntity dateEntity) {
        datePicker.setValue(dateEntity != null ?
                new java.sql.Date(dateEntity.getValue().getTime()).toLocalDate() : null);
    }
}