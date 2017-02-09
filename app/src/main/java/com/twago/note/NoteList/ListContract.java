package com.twago.note.NoteList;

import com.twago.note.Note;

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
    }
}
