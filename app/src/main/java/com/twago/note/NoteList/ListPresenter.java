package com.twago.note.NoteList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.twago.note.Note;
import com.twago.note.NoteEditor.EditorFragment;
import com.twago.note.R;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;

public class ListPresenter implements ListContract.UserActionListener {
    ListContract.View noteListFragmentView;
    Activity activity;
    Realm realm;

    public ListPresenter(Activity activity, ListContract.View noteListFragmentView) {
        this.noteListFragmentView = noteListFragmentView;
        this.realm = Realm.getDefaultInstance();
        this.activity = activity;
    }

    @Override
    public void inflateView() {
        inflateRecyclerView();
    }

    @Override
    public void openNewEditor(int id, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DialogFragment newFragment = EditorFragment.newInstance(id);
        newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        newFragment.show(fragmentTransaction, "");
    }

    private void inflateRecyclerView(){
        noteListFragmentView.setAdapterOnRecyclerViewFromDB(createNewListAdapter());
    }

    @NonNull
    private ListAdapter createNewListAdapter() {
        return new ListAdapter(this,getNotesFromDB(),activity.getFragmentManager());
    }

    private RealmResults<Note> getNotesFromDB() {
        return realm.where(Note.class).findAllSorted(Note.DATE);
    }
}
