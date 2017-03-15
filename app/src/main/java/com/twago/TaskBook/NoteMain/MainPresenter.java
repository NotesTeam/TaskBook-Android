package com.twago.TaskBook.NoteMain;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.twago.TaskBook.NoteEditor.EditorFragment;
import com.twago.TaskBook.R;
import com.twago.TaskBook.TaskBook;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

class MainPresenter implements MainContract.UserActionListener {
    private MainContract.View mainActivityView;

    MainPresenter(MainContract.View mainActivityView) {
        this.mainActivityView = mainActivityView;
    }

    @Override
    public void setInfoBarDate(final boolean isArchiveOpen, FragmentManager fragmentManager) {
        Calendar calendar = TaskBook.getInstance().getCalendar();

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        TaskBook.getInstance().setTimeStamp(calendar.getTimeInMillis());
                        mainActivityView.setCurrentDateInInfoBar();
                        mainActivityView.updateRecyclerView(isArchiveOpen, calendar);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(fragmentManager, "Datepickerdialog");
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
