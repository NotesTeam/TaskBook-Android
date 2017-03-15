package com.twago.TaskBook.NoteEditor;

import android.app.FragmentManager;

public interface EditorContract {
    interface View {
        String getTextNote();

        String getTitleNote();

        int getEditedNoteId();

        void setTitleNoteEditText(String title);

        void setTextNoteEditText(String text);

        void notifyItemAdded(int id);

        void updateNoteColor(int currentColorRes);

        void setEditorBackgroundColor(int currentColorRes);
    }

    interface UserActionListener {

        void inflateExistNoteData();

        void saveNoteToDatabase();

        void updateNoteColor(int currentColorRes);

        void openColorFragment(FragmentManager fragmentManager);
    }
}
