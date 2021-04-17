package com.katyshevtseva.kikiorg.core;

import java.text.ParseException;
import java.util.Date;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;

public class CoreConstants {
    public static final String APP_NAME = "Kiki Org";
    public static Date FINANCIAL_ACCOUNTING_START_DATE;

    static {
        try {
            FINANCIAL_ACCOUNTING_START_DATE = (READABLE_DATE_FORMAT.parse("21.09.2020"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
