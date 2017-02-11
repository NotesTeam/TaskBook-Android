package com.twago.TaskBook.NoteList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;

import com.twago.TaskBook.Constants;
import com.twago.TaskBook.Note;
import com.twago.TaskBook.NoteEditor.EditorFragment;
import com.twago.TaskBook.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.functions.Action1;

public class ListPresenter implements ListContract.UserActionListener {
    private ListContract.View noteListFragmentView;
    private Activity activity;
    private Realm realm;
    private long currentNoteDate = Calendar.getInstance().getTimeInMillis();

    public ListPresenter(Activity activity, final ListContract.View noteListFragmentView) {
        this.noteListFragmentView = noteListFragmentView;
        this.realm = Realm.getDefaultInstance();
        this.activity = activity;
    }

    private void updateRecyclerView(RealmResults<Note> notes) {
        ListAdapter recyclerViewAdapter = noteListFragmentView.getRecyclerViewAdapter();
        recyclerViewAdapter.setData(notes);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void inflateView() {
        inflateRecyclerView();
        inflateInfoBar();
    }

    private void inflateRecyclerView() {
        noteListFragmentView.setAdapterOnRecyclerView(new ListAdapter(this));
    }

    private void inflateInfoBar() {
        setCurrentDate();
    }

    private void setActiveTasksObserver() {
        realm.where(Note.class)
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
        realm.where(Note.class)
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

    private void setCurrentDate() {
        noteListFragmentView.setDateInInfoBar(getFormatedDay(), getFormatedMonth());
    }

    @Override
    public void initialization() {
        inflateView();
        openActiveTasks();
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
        setActiveTasksObserver();
    }

    @Override
    public void openArchive() {
        setArchivedTasksObserver();
    }

    @Override
    public int getTaskIcon(Note note) {
        switch (note.getTask()) {
            case Constants.MAIN_TASK:
                return R.drawable.ic_star_indigo_500_24dp;
            case Constants.PART_TASK:
                return R.drawable.ic_star_half_indigo_500_24dp;
            case Constants.SKILLS_TASK:
                return R.drawable.ic_lightbulb_outline_indigo_500_24dp;
            case Constants.UNIMPORTANT_TASK:
                return R.drawable.ic_help_outline_indigo_500_24dp;
        }
        return 0;
    }

    @Override
    public void deleteNote(final int id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Note.class).equalTo(Note.ID, id).findFirst().setArchived(true);
            }
        });
    }

    @Override
    public String getFormatedDate(Note note) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(new Date(note.getDate()));
    }

    private String getFormatedDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        return simpleDateFormat.format(new Date(currentNoteDate));
    }

    private String getFormatedMonth() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        return simpleDateFormat.format(new Date(currentNoteDate));
    }
}
