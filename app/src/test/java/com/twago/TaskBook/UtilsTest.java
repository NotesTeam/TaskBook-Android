package com.twago.TaskBook;

import org.junit.Test;

import com.twago.TaskBook.Module.Note;

import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wzieba on 2/14/2017.
 */
public class UtilsTest {
    @Test
    public void getFormattedDate() throws Exception {
        Note note = new Note();
        note.setDate(new Date().getTime());
        Assert.assertEquals(TaskBook.getInstance().getFormattedDate(note), new SimpleDateFormat("EEEE dd MMM yyyy", Locale.getDefault()).format(new Date().getTime()));
    }

}