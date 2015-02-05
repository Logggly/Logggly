package com.logggly.utilities;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Hafiz Waleed Hussain on 1/31/2015.
 */
public class DateTimeFormatter {

    public static final String dateFormatter(Calendar calendar){
        return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())+" "+
                calendar.get(Calendar.DAY_OF_MONTH)+", "+calendar.get(Calendar.YEAR);
    }

    public static final String timeFormatter(Calendar calendar){
        String mins =  calendar.get(Calendar.MINUTE) < 10 ?
                "0"+calendar.get(Calendar.MINUTE):
                calendar.get(Calendar.MINUTE)+"";
        return calendar.get(Calendar.HOUR)+":"+
                mins+" "+(calendar.get(Calendar.AM_PM)== Calendar.AM ?"AM":"PM");

    }

}
