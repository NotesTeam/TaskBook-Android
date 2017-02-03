package com.twago.note.NoteList;

import android.app.FragmentManager;

public interface ListContract {
    interface View {
        void setAdapterOnRecyclerViewFromDB(ListAdapter listAdapter);
        void openNoteEditor(int id);
    }

    interface UserActionListener {
        void inflateView();
        void openNewEditor(int id, FragmentManager fragmentManager);
    }
}
