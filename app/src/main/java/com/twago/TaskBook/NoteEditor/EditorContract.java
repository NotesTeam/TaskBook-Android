package com.twago.TaskBook.NoteEditor;

import android.app.FragmentManager;

public interface EditorContract {
    interface View {
        int getEditedNoteId();

        String getTextNote();

        String getTitleNote();

        void notifyItemAdded(int id);

        void setEditorBackgroundColor(int currentColorRes);

        void setTextNoteEditText(String text);

        void setTitleNoteEditText(String title);

        void updateNoteColor(int currentColorRes);
    }

    interface UserActionListener {

        void inflateExistNoteData();

        void openColorFragment(FragmentManager fragmentManager);

        void saveNoteToDatabase();

        void updateNoteColor(int currentColorRes);
    }
}
