package com.twago.TaskBook.NoteList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.util.Log;

import com.twago.TaskBook.Module.Note;
import com.twago.TaskBook.NoteEditor.EditorFragment;
import com.twago.TaskBook.R;
import com.twago.TaskBook.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

public class ListPresenter implements ListContract.UserActionListener {
    private final String TAG = this.getClass().getSimpleName();
    private Activity activity;
    private ListContract.View noteListFragmentView;
    private Realm notesDatabase;
    Observable<Long> timeObservable;
    private Subscription subscription;

    public ListPresenter(Activity activity, final ListContract.View noteListFragmentView) {
        this.activity = activity;
        this.noteListFragmentView = noteListFragmentView;
        this.notesDatabase = Realm.getDefaultInstance();
    }

    @Override
    public void initialization() {
        inflateView();
        openActiveTasks();
    }

    private void inflateView() {
        inflateRecyclerView();
        inflateInfoBar();
    }

    private void inflateRecyclerView() {
        noteListFragmentView.setAdapterOnRecyclerView(new ListAdapter(this));
    }

    private void inflateInfoBar() {
        setTimeObserver();
    }

    private void setActiveTasksObserver() {
        subscription = notesDatabase.where(Note.class)
                .equalTo(Note.IS_ARCHIVED, false)
                .findAllSorted(Note.DATE)
                .asObservable()
                .subscribe(new Action1<RealmResults<Note>>() {
                    @Override
                    public void call(RealmResults<Note> notes) {
                        updateRecyclerView(notes);
                    }
                });
    }

    private void setArchivedTasksObserver() {
        subscription = notesDatabase.where(Note.class)
                .equalTo(Note.IS_ARCHIVED, true)
                .findAllSorted(Note.DATE)
                .asObservable()
                .subscribe(new Action1<RealmResults<Note>>() {
                    @Override
                    public void call(RealmResults<Note> notes) {
                        updateRecyclerView(notes);
                    }
                });
    }

    private void updateRecyclerView(RealmResults<Note> notes) {
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
    public void openActiveTasks() {
        unsubscribeCurrentObserver();
        setActiveTasksObserver();
    }

    @Override
    public void openArchive() {
        unsubscribeCurrentObserver();
        setArchivedTasksObserver();
    }

    private void unsubscribeCurrentObserver() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    @Override
    public void archiveNote(final int id) {
        notesDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Note.class).equalTo(Note.ID, id).findFirst().setArchived(true);
            }
        });

    }

    @Override
    public void deleteNote(final int id) {
        notesDatabase.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Note.class).equalTo(Note.ID, id).findFirst().deleteFromRealm();
            }
        });
    }

    private void setTimeObserver() {
        Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                subscriber.onNext(Utils.currentDate);
            }
        }).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                setCurrentDateInInfoBar();
            }
        });
    }

    @Override
    public void setCurrentDateInInfoBar() {
        noteListFragmentView.setDateInInfoBar(getFormattedDayForInfoBarDate(), getFormattedMonthForInfoBarDate());
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
