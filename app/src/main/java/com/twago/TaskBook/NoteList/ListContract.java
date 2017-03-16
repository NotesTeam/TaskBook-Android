package com.twago.TaskBook.NoteList;

import android.support.v7.widget.RecyclerView;

import com.twago.TaskBook.Module.Note;

import java.util.Calendar;

import io.realm.RealmList;

interface ListContract {
    interface View {

        RecyclerView getRecyclerView();

        ListAdapter getRecyclerViewAdapter();

        void openNewEditor(int id);

        void setRecyclerViewAdapter(ListAdapter listAdapter);

    }

    interface UserActionListener {

        void archiveNote(int id);

        void deleteNote(int id);

        void inflateListFragment();

        void openNewEditor(int id);

        void updateRecyclerView(boolean isArchived, Calendar calendar);

        void updateRecyclerView(RealmList<Note> notes);
    }
}
