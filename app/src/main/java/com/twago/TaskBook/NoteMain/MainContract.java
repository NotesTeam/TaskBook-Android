package com.twago.TaskBook.NoteMain;

import android.app.FragmentManager;

public interface MainContract {
    interface View {

    }

    interface UserActionListener {

        void setInfoBarDate(boolean isArchiveOpen);

        void openNewEditor(int id, FragmentManager fragmentManager);
    }
}
