package com.twago.TaskBook.NoteList;

import android.support.v7.widget.RecyclerView;

import com.twago.TaskBook.Module.Note;

import java.util.Calendar;

import io.realm.RealmList;
import io.realm.RealmResults;

public interface ListContract {
    interface View {

        ListAdapter getRecyclerViewAdapter();

        void setRecyclerViewAdapter(ListAdapter listAdapter);

        void setDateInInfoBar(String dayText, String monthText);

        void openNewEditor(int id);

        RecyclerView getRecyclerView();
    }

    interface UserActionListener {

        void inflateListFragment();

        void updateRecyclerView(RealmList<Note> notes);

        void showNoteListForDate(boolean isArchived, Calendar calendar);

        void setCurrentDateInInfoBar();

        void deleteNote(int id);

        void archiveNote(int id);

        void openNewEditor(int id);
    }
}
