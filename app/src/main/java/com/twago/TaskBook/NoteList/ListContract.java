package com.twago.TaskBook.NoteList;

import com.twago.TaskBook.Module.Note;

import io.realm.RealmList;
import io.realm.RealmResults;

public interface ListContract {
    interface View {

        ListAdapter getRecyclerViewAdapter();

        void setAdapterOnRecyclerView(ListAdapter listAdapter);

        void setDateInInfoBar(String dayText, String monthText);

        void openNewEditor(int id);
    }

    interface UserActionListener {

        void inflateListFragment();

        void updateRecyclerView(RealmList<Note> notes);

        void setCurrentDateInInfoBar();

        void deleteNote(int id);

        void archiveNote(int id);

        void openNewEditor(int id);

        int getNotesSize();
    }
}
