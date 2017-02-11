package com.twago.note.NoteList;

import com.twago.note.Note;
import com.twago.note.NoteEditor.EditorFragment;

public interface ListContract {
    interface View {

        ListAdapter getRecyclerViewAdapter();

        void setAdapterOnRecyclerView(ListAdapter listAdapter);

        void setDateInInfoBar(String dayText, String monthText);
    }

    interface UserActionListener {
        void initialization();

        void openNewEditor(int id);

        String getFormatedDate(Note note);

        int getTaskIcon(Note note);

        void deleteNote(int id);

        void openActiveTasks();

        void openArchive();
    }
}
