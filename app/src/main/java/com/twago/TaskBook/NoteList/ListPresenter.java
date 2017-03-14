package com.twago.TaskBook.NoteList;

import android.app.Activity;

import com.twago.TaskBook.Module.Note;
import com.twago.TaskBook.NoteMain.MainInterface;
import com.twago.TaskBook.TaskBook;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ListPresenter implements ListContract.UserActionListener {
    private final String TAG = this.getClass().getSimpleName();

    private Activity activity;
    private MainInterface mainInterface;
    private ListContract.View noteListFragmentView;
    private Realm realm;
    private Calendar calendar = Calendar.getInstance();

    public ListPresenter(Activity activity, MainInterface mainInterface, final ListContract.View noteListFragmentView) {
        this.activity = activity;
        this.mainInterface = mainInterface;
        this.noteListFragmentView = noteListFragmentView;
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    public void inflateListFragment() {
        setAdapter();
        inflateInfoBar();
        showNoteListForDate(false, calendar);
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
        noteListFragmentView.setRecyclerViewAdapter(new ListAdapter(this));
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
    public void showNoteListForDate(boolean isArchived, Calendar calendar){
        updateRecyclerView(getNoteList(isArchived, calendar));
    }

    private RealmList<Note> getNoteList(boolean isArchived, Calendar calendar) {
        long dayBegin = getDayBegin(calendar);
        long dayEnd = getDayEnd(calendar);
        RealmResults<Note> realmResults = realm.where(Note.class)
                .greaterThanOrEqualTo(Note.DATE,dayBegin)
                .lessThanOrEqualTo(Note.DATE,dayEnd)
                .equalTo(Note.IS_ARCHIVED, isArchived)
                .findAll();
        RealmList<Note> notes = new RealmList<>();
        notes.addAll(realmResults);
        return notes;
    }

    @Override
    public void updateRecyclerView(RealmList<Note> notes) {
        ListAdapter recyclerViewAdapter = noteListFragmentView.getRecyclerViewAdapter();
        recyclerViewAdapter.setData(notes);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void openNewEditor(int id) {
        noteListFragmentView.openNewEditor(id);
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
}
