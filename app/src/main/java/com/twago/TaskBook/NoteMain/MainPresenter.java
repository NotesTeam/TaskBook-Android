package com.twago.TaskBook.NoteMain;

import android.app.Activity;
import android.widget.Button;

import com.twago.TaskBook.Module.Note;
import com.twago.TaskBook.NoteList.ListContract;
import com.twago.TaskBook.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscription;
import rx.functions.Action1;

public class MainPresenter implements MainContract.UserActionListener {
    private Activity activity;
    private Subscription subscription;
    private Realm realm;
    private ListContract.UserActionListener listPresenter;

    public MainPresenter(Activity activity, ListContract.UserActionListener listPresenter, Realm realm) {
        this.activity = activity;
        this.listPresenter = listPresenter;
        this.realm = realm;
    }

    @Override
    public void setupSubscriberActiveTasks() {
        unsubscribeCurrentObserver();
        setActiveTasksObserver();
    }

    @Override
    public void setupSubscriberArchive() {
        unsubscribeCurrentObserver();
        setArchivedTasksObserver();
    }

    private void setActiveTasksObserver() {
        subscription = realm.where(Note.class)
                .equalTo(Note.IS_ARCHIVED, false)
                .findAllSorted(Note.DATE)
                .asObservable()
                .subscribe(new Action1<RealmResults<Note>>() {
                    @Override
                    public void call(RealmResults<Note> notes) {
                        listPresenter.updateRecyclerView(notes);
                    }
                });
    }

    private void setArchivedTasksObserver() {
        subscription = realm.where(Note.class)
                .equalTo(Note.IS_ARCHIVED, true)
                .equalTo(Note.DATE,Utils.getCurrentDayMonthYearDate())
                .findAllSorted(Note.DATE)
                .asObservable()
                .subscribe(new Action1<RealmResults<Note>>() {
                    @Override
                    public void call(RealmResults<Note> notes) {
                        listPresenter.updateRecyclerView(notes);
                    }
                });
    }

    private void unsubscribeCurrentObserver() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    @Override
    public void setInfoBarDate() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(Utils.currentDate);

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        Utils.currentDate = calendar.getTimeInMillis();
                        listPresenter.setCurrentDateInInfoBar();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(activity.getFragmentManager(), "Datepickerdialog");
    }
}
