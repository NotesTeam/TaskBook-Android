package com.twago.note;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;


public class NoteEditorFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG_ID = "TAG_ID";
    private static final String TAG = NoteEditorFragment.class.getSimpleName();
    private int noteId;
    private String task = "";
    private long currentNoteDate = Calendar.getInstance().getTimeInMillis();
    @BindView(R.id.edit_note_background)
    LinearLayout editNoteBackground;
    @BindView(R.id.button_save_note)
    Button saveNoteButton;
    @BindView(R.id.title_edit_note)
    EditText titleNoteEdit;
    @BindView(R.id.text_edit_note)
    EditText textNoteEdit;
    @BindView(R.id.main_task_note_editor)
    ImageView mainTaskButton;
    @BindView(R.id.part_task_note_editor)
    ImageView partTaskButton;
    @BindView(R.id.skills_task_note_editor)
    ImageView skillsTaskButton;
    @BindView(R.id.unimportant_task_note_editor)
    ImageView unimportantTaskButton;

    public static NoteEditorFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(TAG_ID, id);
        NoteEditorFragment fragment = new NoteEditorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_note_editor, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.main_task_note_editor, R.id.part_task_note_editor,
            R.id.skills_task_note_editor, R.id.unimportant_task_note_editor})
    public void pickTaskNote(ImageView taskView) {

        switch (taskView.getId()) {
            case R.id.main_task_note_editor:
                task = Constants.MAIN_TASK;
                break;
            case R.id.part_task_note_editor:
                task = Constants.PART_TASK;
                break;
            case R.id.skills_task_note_editor:
                task = Constants.SKILLS_TASK;
                break;
            case R.id.unimportant_task_note_editor:
                task = Constants.UNIMPORTANT_TASK;
                break;
        }
        saveNoteButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button_set_date)
    public void pickDate() {
        Note note = getNoteWithId(noteId);
        Calendar calendar = Calendar.getInstance();;
        if (note != null)
            calendar.setTimeInMillis(note.getDate());

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getChildFragmentManager(), "Datepickerdialog");
    }

    @OnClick(R.id.button_save_note)
    public void saveNote() {
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        noteId = getArguments().getInt("TAG_ID");
        if (!isNoteNew())
            inflateNoteData(noteId);
    }


    private boolean isNoteNew() {
        return noteId == Constants.NEW_NOTE_ID;
    }

    private void inflateNoteData(int noteId) {
        Note note = getNoteWithId(noteId);
        inflateNoteData(note);
    }

    private void inflateNoteData(Note note) {
        if (note != null) {
            titleNoteEdit.setText(note.getTitle());
            textNoteEdit.setText(note.getText());
            task = note.getTask();
            currentNoteDate = note.getDate();
            if (!task.equals(""))
                saveNoteButton.setVisibility(View.VISIBLE);
        }
    }

    private void createNewNote(Realm realm) {
        Note note = new Note(generateNewId(realm), titleNoteEdit.getText().toString(),
                textNoteEdit.getText().toString(), task, currentNoteDate);
        realm.copyToRealm(note);
    }

    private void saveNoteToDatabase() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (isNoteNew()) {
                    if (!isNoteEmpty())
                        createNewNote(realm);
                } else
                    updateNote(realm);
            }
        });
    }

    private void updateNote(Realm realm) {
        Note note = realm.where(Note.class).equalTo(Note.ID, noteId).findFirst();
        if (note != null) {
            note.setTitle(titleNoteEdit.getText().toString());
            note.setText(textNoteEdit.getText().toString());
            note.setTask(task);
            note.setDate(currentNoteDate);
        }
    }

    private int generateNewId(Realm realm) {
        RealmResults<Note> results = realm.where(Note.class).findAll();
        if (results.isEmpty()) return 0;
        return results.max(Note.ID).intValue() + 1;
    }

    private Note getNoteWithId(int noteId) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Note.class).equalTo(Note.ID, noteId).findFirst();
    }

    private boolean isNoteEmpty() {
        return titleNoteEdit.getText().toString().equals("") && textNoteEdit.getText().toString().equals("");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        saveNoteToDatabase();
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof DialogInterface.OnDismissListener)
            ((DialogInterface.OnDismissListener) parentFragment).onDismiss(dialog);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        currentNoteDate = calendar.getTimeInMillis();
        Log.d(TAG, String.format("current note date: %d%n", currentNoteDate));
    }
}

