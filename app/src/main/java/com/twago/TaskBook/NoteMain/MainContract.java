package com.twago.TaskBook.NoteMain;

import android.app.FragmentManager;

import java.util.Calendar;

interface MainContract {
    interface View {
        void setCurrentDateInInfoBar();

        void updateRecyclerView(boolean isArchiveOpen, Calendar calendar);
    }

    interface UserActionListener {

        void openNewEditor(int id, FragmentManager fragmentManager);

        void setInfoBarDate(boolean isArchiveOpen, FragmentManager fragmentManager);
    }
}
