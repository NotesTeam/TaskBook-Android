package com.twago.note.NoteList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;

import com.twago.note.Constants;
import com.twago.note.Note;
import com.twago.note.NoteEditor.EditorFragment;
import com.twago.note.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class ListPresenter implements ListContract.UserActionListener {
    private ListContract.View noteListFragmentView;
    private Activity activity;
    private Realm realm;
    private long currentNoteDate = Calendar.getInstance().getTimeInMillis();

    ListPresenter(Activity activity, ListContract.View noteListFragmentView) {
        this.noteListFragmentView = noteListFragmentView;
        this.realm = Realm.getDefaultInstance();
        this.activity = activity;
    }

    @Override
    public void inflateView() {
        inflateRecyclerView();
        setCurrentDateInInfoBar();
    }

    private void setCurrentDateInInfoBar(){
        noteListFragmentView.setDateInInfoBar(getFormatedDay(),getFormatedMonth());
    }

    private void inflateRecyclerView(){
        noteListFragmentView.setAdapterOnRecyclerViewFromDB(createNewListAdapter());
    }

    @Override
    public void openNewEditor(int id) {
        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        DialogFragment newFragment = EditorFragment.newInstance(id);
        newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        newFragment.show(fragmentTransaction, "");
    }

    private ListAdapter createNewListAdapter() {
        return new ListAdapter(this,getNotesFromDB());
    }

    private RealmResults<Note> getNotesFromDB() {
        return realm.where(Note.class).findAllSorted(Note.DATE);
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
    public String getFormatedDate(Note note) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(new Date(note.getDate()));
    }

    private String getFormatedDay(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        return simpleDateFormat.format(new Date(currentNoteDate));
    }
    private String getFormatedMonth(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        return simpleDateFormat.format(new Date(currentNoteDate));
    }
}
