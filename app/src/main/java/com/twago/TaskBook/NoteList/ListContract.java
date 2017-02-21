package com.twago.TaskBook.NoteList;

import com.twago.TaskBook.Module.Note;

import io.realm.RealmResults;

public interface ListContract {
    interface View {

        ListAdapter getRecyclerViewAdapter();

        void setAdapterOnRecyclerView(ListAdapter listAdapter);

        void setDateInInfoBar(String dayText, String monthText);
    }

    interface UserActionListener {

        void inflateListFragment();

        void updateRecyclerView(RealmResults<Note> notes);

        void openNewEditor(int id);

        void setCurrentDateInInfoBar();

        void deleteNote(int id);

        void archiveNote(int id);
    }
}
