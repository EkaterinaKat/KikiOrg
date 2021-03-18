package com.katyshevtseva.kikiorg.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CoreConstants {
    public static final String APP_NAME = "Kiki Org";
    public static Date FINANCIAL_ACCOUNTING_START_DATE;

    static {
        try {
            FINANCIAL_ACCOUNTING_START_DATE = (new SimpleDateFormat("dd.MM.yyyy")).parse("21.09.2020");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
