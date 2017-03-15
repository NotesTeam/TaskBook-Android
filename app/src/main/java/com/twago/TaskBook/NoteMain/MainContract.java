package com.twago.TaskBook.NoteMain;

import android.app.FragmentManager;

interface MainContract {
    interface View {}

    interface UserActionListener {

        void openNewEditor(int id, FragmentManager fragmentManager);

        void setInfoBarDate(boolean isArchiveOpen);
    }
}
