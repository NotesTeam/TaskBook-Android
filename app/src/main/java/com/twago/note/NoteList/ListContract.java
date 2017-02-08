package com.twago.note.NoteList;

import android.app.FragmentManager;
import android.widget.TextView;

import com.twago.note.Note;

public interface ListContract {
    interface View {
        void setAdapterOnRecyclerViewFromDB(ListAdapter listAdapter);

        void setDateInInfoBar(String dayText, String monthText);
    }

    interface UserActionListener {
        void inflateView();

        void openNewEditor(int id);

        String getFormatedDate(Note note);
        int getTaskIcon(Note note);
    }
}
