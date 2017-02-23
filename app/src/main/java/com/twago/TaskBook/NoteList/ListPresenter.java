package com.twago.TaskBook.NoteList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;

import com.twago.TaskBook.Module.Note;
import com.twago.TaskBook.NoteEditor.EditorFragment;
import com.twago.TaskBook.R;
import com.twago.TaskBook.TaskBook;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ListPresenter implements ListContract.UserActionListener {
    private final String TAG = this.getClass().getSimpleName();
    private Activity activity;
    private ListContract.View noteListFragmentView;
    private Realm realm;
    private Calendar calendar = Calendar.getInstance();

    public ListPresenter(Activity activity, final ListContract.View noteListFragmentView) {
        this.activity = activity;
        this.noteListFragmentView = noteListFragmentView;
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    public void inflateListFragment() {
        setAdapter();
        inflateInfoBar();
        showNotesForDate(calendar);
    }

    private void showNotesForDate(Calendar calendar){
        long dayBegin = getDayBegin(calendar);
        long dayEnd = getDayEnd(calendar);
        RealmResults<Note> realmResults = realm.where(Note.class)
                .greaterThanOrEqualTo(Note.DATE,dayBegin)
                .lessThanOrEqualTo(Note.DATE,dayEnd)
                .findAll();
        RealmList<Note> notes = new RealmList<>();
        notes.addAll(realmResults);
        updateRecyclerView(notes);
    }

    private long getDayBegin(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                0,0,0);
        return calendar.getTimeInMillis();
    }

    private long getDayEnd(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                23,59,59);
        return calendar.getTimeInMillis();
    }

    private void setAdapter() {
        noteListFragmentView.setAdapterOnRecyclerView(new ListAdapter(this));
    }

    private void inflateInfoBar() {
        setCurrentDateInInfoBar();
    }

    @Override
    public void setCurrentDateInInfoBar() {
        noteListFragmentView.setDateInInfoBar(
                TaskBook.getInstance().getFormattedDayForInfoBarDate(),
                TaskBook.getInstance().getFormattedMonthForInfoBarDate());
    }

    @Override
    public void updateRecyclerView(RealmList<Note> notes) {
        ListAdapter recyclerViewAdapter = noteListFragmentView.getRecyclerViewAdapter();
        recyclerViewAdapter.setData(notes);
        recyclerViewAdapter.notifyDataSetChanged();
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
    public void openNewEditor(int id) {
        noteListFragmentView.openNewEditor(id);
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
}
