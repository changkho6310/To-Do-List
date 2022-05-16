package com.example.myapplication.Utils;

import static com.example.myapplication.Utils.Database.DEADLINE_FORMAT;
import static com.example.myapplication.Utils.Database.NAME;
import static com.example.myapplication.Utils.Database.VERSION;

import android.util.Log;

import com.example.myapplication.Adapter.TaskAdapter;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Helpers {
    public static DateFormat dateFormat = new SimpleDateFormat(DEADLINE_FORMAT);

    public static Date convertStringToDate(String strDate) {
        // strDate follow DATE_FORMAT : dd/MM/yyyy
        // Time of Date return will be 23:59:59
        DateFormat dateFormat2 = new SimpleDateFormat(DEADLINE_FORMAT + " hh:mm:ss");
        Date result = null;
        try {
            result = dateFormat2.parse(strDate + " 23:59:59");
        } catch (ParseException e) {
            Log.d("AAA", "convertStringToDate: " + e.toString());
        }
        return result;
    }

    public static boolean isDateAfterNow(String strDate) {
        Date date = Helpers.convertStringToDate(strDate);
        if (date != null) {
            Date now = new Date();
            if (date.compareTo(now) >= 0) {
                return true;
            }
        }
        return false;
    }

    public static String convertDateToString(Date date) {
        return dateFormat.format(date);
    }

    public static Date getFirstDayOfThisWeek() {
        Calendar calendar = Calendar.getInstance();
        // clear would not reset the hour of day !
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    public static Date getFirstDayOfThisMonth() {
        Calendar calendar = Calendar.getInstance();
        // clear would not reset the hour of day !
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
}
