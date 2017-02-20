package com.twago.TaskBook;

import android.util.Log;

import com.twago.TaskBook.Module.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wzieba on 2/14/2017.
 */
public class Utils {
    public static long currentDate = Calendar.getInstance().getTimeInMillis();

    public static String getFormattedDate(Note note) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(new Date(note.getDate()));
    }

    private static Calendar getCalendar(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar;
    }

    private static int getDay(long date){
        return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }
    private static int getMonth(long date){
        return getCalendar(date).get(Calendar.MONTH);
    }
    private static int getYear(long date){
        return getCalendar(date).get(Calendar.YEAR);
    }

    private static long getDayMonthYearDate(long date) {
        Calendar calendar = getCalendar(0);
        calendar.set(
                getYear(date),
                getMonth(date),
                getDay(date),
                0,0,0
        );
        Log.d("Utils: ", String.valueOf(calendar.getTimeInMillis()));
        return calendar.getTimeInMillis();
    }

    public static long getCurrentDayMonthYearDate() {
        return getDayMonthYearDate(currentDate);
    }
}
