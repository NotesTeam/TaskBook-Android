package com.twago.TaskBook.NoteMain;

interface MainContract {
    interface View{

    }
    interface UserActionListener {
        void setInfoBarDate();

        long getCurrentListDate();
    }
}
