package com.twago.TaskBook.NoteMain;

import com.twago.TaskBook.NoteEditor.EditorContract;

public interface MainInterface {

    void notifyItemAdded(int id);

    void notifyDataSetChanged();

    void openNewEditor(int id);

    void pickDate();

    void setEditorFragmentView(EditorContract.View editorFragmentView);

    void updateNoteColor(int currentColorRes);
}