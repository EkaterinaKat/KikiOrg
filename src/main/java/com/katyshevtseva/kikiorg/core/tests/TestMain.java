package com.katyshevtseva.kikiorg.core.tests;

public class TestMain {
    public static void main(String[] args) {
        System.out.println("60 = " + getHoursMinutesStringByMinutes(60));
        System.out.println("70 = " + getHoursMinutesStringByMinutes(70));
        System.out.println("120 = " + getHoursMinutesStringByMinutes(120));
        System.out.println("150 = " + getHoursMinutesStringByMinutes(150));
        System.out.println("3 = " + getHoursMinutesStringByMinutes(3));
        System.out.println("180 = " + getHoursMinutesStringByMinutes(180));
        System.out.println("182 = " + getHoursMinutesStringByMinutes(182));
    }

    static String getHoursMinutesStringByMinutes(int min) {
        int hours = min / 60;
        int leftMin = min % 60;
        if (leftMin == 0)
            return hours + "";
        return String.format("%d:%02d", hours, leftMin);
    }
}
