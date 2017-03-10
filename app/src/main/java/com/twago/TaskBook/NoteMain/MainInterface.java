package com.twago.TaskBook.NoteMain;

import java.util.Calendar;

/**
 * Created by twago on 22.02.17.
 */

public interface MainInterface {

    void openNewEditor(int id);

    void showNoteListForDate(boolean isArchived, Calendar calendar);

    void notifyItemAdded(int id);

    void setInfoBarDate();
}