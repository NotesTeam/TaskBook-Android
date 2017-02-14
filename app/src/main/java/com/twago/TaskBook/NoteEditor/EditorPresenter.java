package com.twago.TaskBook.NoteEditor;

import android.app.Activity;

import com.twago.TaskBook.Module.Constants;
import com.twago.TaskBook.Module.Note;
import com.twago.TaskBook.NoteMain.NoteMainActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

class EditorPresenter implements EditorContract.UserActionListener {
    private static final String TAG = EditorPresenter.class.getSimpleName();
    private int chosenNoteId;
    private long currentNoteDate;
    private NoteMainActivity activity;
    private EditorContract.View noteEditFragmentView;

    EditorPresenter(NoteMainActivity activity, EditorContract.View noteEditFragmentView) {
        this.activity = activity;
        this.noteEditFragmentView = noteEditFragmentView;
        this.chosenNoteId = noteEditFragmentView.getChosenNoteId();
        this.currentNoteDate = activity.sendCurrentListDate();
    }

    @Override
    public void inflateChosenNoteData() {
        if (!isNoteNew()) {
            Note chosenNote = getNoteWithId(chosenNoteId);
            inflateNoteData(chosenNote);
            if (chosenNote.isArchived())
                noteEditFragmentView.blockArchivedNoteViews();
        }
    }

    private void inflateNoteData(Note note) {
        if (note != null) {
            noteEditFragmentView.setTitleNoteEditText(note.getTitle());
            noteEditFragmentView.setTextNoteEditText(note.getText());
            currentNoteDate = note.getDate();
        }
    }

    @Override
    public void saveNoteToDatabase() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (isNoteNew())
                    createNewNote(realm);
                else
                    updateNote();
            }
        });
    }

    private void createNewNote(Realm realm) {
        if (!isNoteEmpty()) {
            Note note = new Note(generateNewId(realm), noteEditFragmentView.getTitleNote(),
                    noteEditFragmentView.getTextNote(), currentNoteDate, false);
            realm.copyToRealm(note);
        }
    }

    private int generateNewId(Realm realm) {
        RealmResults<Note> results = realm.where(Note.class).findAll();
        if (results.isEmpty()) return 0;
        return results.max(Note.ID).intValue() + 1;
    }

    private void updateNote() {
        Note chosenNote = getNoteWithId(chosenNoteId);
        chosenNote.setTitle(noteEditFragmentView.getTitleNote());
        chosenNote.setText(noteEditFragmentView.getTextNote());
        chosenNote.setDate(currentNoteDate);
    }


    private Note getNoteWithId(int noteId) {
        return Realm.getDefaultInstance().where(Note.class).equalTo(Note.ID,noteId).findFirst();
    }

    private boolean isNoteNew() {
        return chosenNoteId == Constants.NEW_NOTE_ID;
    }

    private boolean isNoteEmpty() {
        return noteEditFragmentView.getTitleNote().equals("") && noteEditFragmentView.getTextNote().equals("");
    }

    @Override
    public void setCurrentNoteDate() {
        Calendar calendar = Calendar.getInstance();

        Note chosenNote = getNoteWithId(chosenNoteId);
        if (chosenNote != null)
            calendar.setTimeInMillis(chosenNote.getDate());

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        currentNoteDate = calendar.getTimeInMillis();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(activity.getFragmentManager(), "Datepickerdialog");
    }
}
