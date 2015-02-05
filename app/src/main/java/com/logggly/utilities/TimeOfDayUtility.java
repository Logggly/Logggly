package com.logggly.utilities;

import java.util.Calendar;

/**
 * Created by Hafiz Waleed Hussain on 2/1/2015.
 */
public class TimeOfDayUtility {

    public static final String timeOfDay(int hour, int min, int AM_PM) {

        int total = hour * 60 + min;

        //  7:01-9:00
        if ( 7 * 60 + 1 <= total   && total <= 9 * 60 + 0 && AM_PM == Calendar.AM) {
            return "EarlyMorn";
        }
        //  9:01-10:30
        else if ( 9 * 60 + 1 <= total && total <= 10 * 60 + 30 && AM_PM == Calendar.AM) {
            return "MidMorn";
        } else if (10 * 60 + 31 <= total && total <= 11 * 59 + 0 && AM_PM == Calendar.AM) {
            return "LateMorn";
        } else if (0 * 60 + 0 <= total && total <= 1 * 60 + 30 && AM_PM == Calendar.PM) {
            return "EarlyAft";
        } else if (1 * 60 + 31 <= total && total <= 3 * 60 + 0 && AM_PM == Calendar.PM) {
            return "MidAft";
        } else if (3 * 60 + 1 <= total && total <= 5 * 60 + 0 && AM_PM == Calendar.PM) {
            return "LateAft";
        } else if (5 * 60 + 1 <= total && total <= 9 * 60 + 0 && AM_PM == Calendar.PM) {
            return "Evening";
        } else {
            return "NightTime";
        }


    }
}
