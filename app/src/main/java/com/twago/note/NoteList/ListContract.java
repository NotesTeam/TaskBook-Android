package com.twago.note.NoteList;

import android.app.FragmentManager;

import com.twago.note.Note;

import io.realm.RealmResults;

/**
 * Created by twago on 01.02.17.
 */

public interface ListContract {
    interface View {
        void setDeleteButtonVisibility(int visibility);
        void setCreateButtonVisibility(int visibility);
        void setAdapterOnRecyclerViewFromDB(ListAdapter listAdapter);
        void openNoteEditor(int id);
    }

    interface UserActionListener {
        void toggleCheckNoteInDB(final int id);
        void inflateView();
        void deleteCheckedNotes();
        void openNewEditor(int id, FragmentManager fragmentManager);
    }
}
