package com.twago.TaskBook.NoteList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;

import com.twago.TaskBook.Module.Note;
import com.twago.TaskBook.NoteEditor.EditorFragment;
import com.twago.TaskBook.R;
import com.twago.TaskBook.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class ListPresenter implements ListContract.UserActionListener {
    private final String TAG = this.getClass().getSimpleName();
    private Activity activity;
    private ListContract.View noteListFragmentView;
    private Realm realm;

    public ListPresenter(Activity activity, final ListContract.View noteListFragmentView) {
        this.activity = activity;
        this.noteListFragmentView = noteListFragmentView;
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    public void inflateListFragment() {
        inflateRecyclerView();
        inflateInfoBar();
    }

    private void inflateRecyclerView() {
        noteListFragmentView.setAdapterOnRecyclerView(new ListAdapter(this));
    }

    private void inflateInfoBar() {
        setCurrentDateInInfoBar();
    }

    @Override
    public void setCurrentDateInInfoBar() {
        noteListFragmentView.setDateInInfoBar(getFormattedDayForInfoBarDate(), getFormattedMonthForInfoBarDate());
    }

    @Override
    public void updateRecyclerView(RealmResults<Note> notes) {
        ListAdapter recyclerViewAdapter = noteListFragmentView.getRecyclerViewAdapter();
        recyclerViewAdapter.setData(notes);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void openNewEditor(int id) {
        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        DialogFragment newFragment = EditorFragment.newInstance(id);
        newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        newFragment.show(fragmentTransaction, "");
    }

    @Override
    public void archiveNote(final int id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Note.class).equalTo(Note.ID, id).findFirst().setArchived(true);
            }
        });

    }

    @Override
    public void deleteNote(final int id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Note.class).equalTo(Note.ID, id).findFirst().deleteFromRealm();
            }
        });
    }

    private String getFormattedDayForInfoBarDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        return simpleDateFormat.format(new Date(Utils.currentDate));
    }

    private String getFormattedMonthForInfoBarDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        return simpleDateFormat.format(new Date(Utils.currentDate)).toUpperCase();
    }
}
