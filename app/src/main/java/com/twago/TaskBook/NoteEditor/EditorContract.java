package com.twago.TaskBook.NoteEditor;

public interface EditorContract {
    interface View {
        String getTextNote();

        String getTitleNote();

        int getEditedNoteId();

        void setTitleNoteEditText(String title);

        void setTextNoteEditText(String text);
    }

    interface UserActionListener {

        void setCurrentNoteDate();

        void inflateExistNoteData();

        void saveNoteToDatabase();
    }
}
