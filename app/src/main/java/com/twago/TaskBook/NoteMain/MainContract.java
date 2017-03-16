package com.twago.TaskBook.NoteMain;

import android.app.FragmentManager;

import java.util.Calendar;

interface MainContract {
    interface View {

        void updateRecyclerView(boolean isArchiveOpen, Calendar calendar);

        void setDateInInfoBar(String formattedDayForInfoBarDate, String formattedMonthForInfoBarDate);
    }

    interface UserActionListener {

        void setDateInInfoBar();

        void openNewEditor(int id, FragmentManager fragmentManager);

        void setDate(boolean isArchiveOpen, FragmentManager fragmentManager);
    }
}
