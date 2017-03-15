package com.twago.TaskBook.NoteMain;

import com.twago.TaskBook.NoteEditor.EditorContract;
import com.twago.TaskBook.NoteEditor.EditorFragment;

import java.util.Calendar;

/**
 * Created by twago on 22.02.17.
 */

public interface MainInterface {

    void choseDate();

    void openNewEditor(int id);

    void showNoteListForDate(boolean isArchived, Calendar calendar);

    void notifyItemAdded(int id);

    void setInfoBarDate();

    void updateNoteColor(int currentColorRes);

    void setEditorFragmentView(EditorContract.View editorFragmentView);

    void notifyDataSetChanged();
}