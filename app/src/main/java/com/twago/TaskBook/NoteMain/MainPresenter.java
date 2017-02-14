package com.twago.TaskBook.NoteMain;

import android.app.Activity;

import com.twago.TaskBook.NoteList.ListContract;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class MainPresenter implements MainContract.UserActionListener {
    private long currentInfoBarDate = Calendar.getInstance().getTimeInMillis();
    private Activity activity;
    private ListContract.UserActionListener noteListUserActionListener;

    public MainPresenter(Activity activity, ListContract.UserActionListener noteListUserActionListener) {
        this.activity = activity;
        this.noteListUserActionListener = noteListUserActionListener;
    }

    @Override
    public void setInfoBarDate() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        currentInfoBarDate = calendar.getTimeInMillis();
                        noteListUserActionListener.setCurrentDateInInfoBar(currentInfoBarDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(activity.getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public long getCurrentListDate() {
        return currentInfoBarDate;
    }
}
