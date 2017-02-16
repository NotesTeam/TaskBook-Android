package com.twago.TaskBook.NoteEditor;

public interface EditorContract {
    interface View {
        String getTextNote();

        String getTitleNote();

        void blockArchivedNoteViews();

        int getChosenNoteId();

        void setTitleNoteEditText(String title);

        void setTextNoteEditText(String text);
    }

    interface UserActionListener {

        void setCurrentNoteDate();

        void inflateChosenNoteData();

        void saveNoteToDatabase();
    }
}
