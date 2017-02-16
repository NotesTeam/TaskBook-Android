package com.twago.TaskBook.NoteList;

public interface ListContract {
    interface View {

        ListAdapter getRecyclerViewAdapter();

        void setAdapterOnRecyclerView(ListAdapter listAdapter);

        void setDateInInfoBar(String dayText, String monthText);
    }

    interface UserActionListener {
        void initialization();

        void openNewEditor(int id);

        void setCurrentDateInInfoBar();

        void deleteNote(int id);

        void openActiveTasks();

        void openArchive();

        void archiveNote(int id);
    }
}
