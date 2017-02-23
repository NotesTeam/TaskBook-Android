package com.twago.TaskBook.NoteEditor;

public interface EditorContract {
    interface View {
        String getTextNote();

        String getTitleNote();

        int getEditedNoteId();

        void setTitleNoteEditText(String title);

        void setTextNoteEditText(String text);

        void notifyItemAdded(int id);
    }

    interface UserActionListener {

        void setCurrentNoteDate();

        void inflateExistNoteData();

        void saveNoteToDatabase();
    }
}
