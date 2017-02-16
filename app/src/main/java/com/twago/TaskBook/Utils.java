package com.twago.TaskBook;

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
}
