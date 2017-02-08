package com.twago.note.NoteEditor;

import android.widget.EditText;
import android.widget.ImageView;

public interface EditorContract {
    interface View {
        String getTextNote();
        String getTitleNote();
        EditText getTitleNoteEditText();
        EditText getTextNoteEditText();

        int getNoteId();
    }

    interface UserActionListener {
        void pickTaskNote(ImageView taskIcon);

        void pickDate(EditorFragment editorFragment);

        void inflateOldData();

        void saveNoteToDatabase();
    }
}
