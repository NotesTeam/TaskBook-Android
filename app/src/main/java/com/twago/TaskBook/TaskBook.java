package com.twago.TaskBook;

import android.app.Application;

import com.twago.TaskBook.Module.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskBook extends Application {
    private static TaskBook instance = null;
    private long timeStamp = new Date().getTime();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public String getFormattedDayForInfoBarDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        return simpleDateFormat.format(new Date(TaskBook.getInstance().getTimeStamp()));
    }

    public String getFormattedMonthForInfoBarDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        return simpleDateFormat.format(new Date(TaskBook.getInstance().getTimeStamp())).toUpperCase();
    }

    public static String getFormattedDate(Note note) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(new Date(note.getDate()));
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Calendar getCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getTimeStamp());
        return calendar;
    }

    public static TaskBook getInstance() {
        return instance;
    }
}
