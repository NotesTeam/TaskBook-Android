package com.twago.TaskBook.NoteMain;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.twago.TaskBook.NoteEditor.EditorFragment;
import com.twago.TaskBook.NoteList.ListContract;
import com.twago.TaskBook.R;
import com.twago.TaskBook.TaskBook;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import io.realm.Realm;

public class MainPresenter implements MainContract.UserActionListener {
    private Activity activity;
    private ListContract.UserActionListener listPresenter;

    public MainPresenter(Activity activity, ListContract.UserActionListener listPresenter, Realm realm) {
        this.activity = activity;
        this.listPresenter = listPresenter;
    }

    @Override
    public void setInfoBarDate(final boolean isArchiveOpen) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(TaskBook.getInstance().getTimeStamp());

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        TaskBook.getInstance().setTimeStamp(calendar.getTimeInMillis());
                        listPresenter.setCurrentDateInInfoBar();
                        listPresenter.updateRecyclerView(isArchiveOpen, calendar);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(activity.getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void openNewEditor(int id, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        DialogFragment newFragment = EditorFragment.newInstance(id);
        newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        newFragment.show(fragmentTransaction, "");
    }
}
