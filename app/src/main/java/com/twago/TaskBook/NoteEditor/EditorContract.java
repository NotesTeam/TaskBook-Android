package com.twago.TaskBook.NoteEditor;

import com.twago.TaskBook.Module.Note;

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
