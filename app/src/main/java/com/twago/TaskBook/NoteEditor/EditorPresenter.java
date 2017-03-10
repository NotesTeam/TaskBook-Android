package com.twago.TaskBook.NoteEditor;

import com.twago.TaskBook.Module.Constants;
import com.twago.TaskBook.Module.Note;
import com.twago.TaskBook.NoteList.ListContract;
import com.twago.TaskBook.NoteMain.MainContract;
import com.twago.TaskBook.NoteMain.MainInterface;
import com.twago.TaskBook.NoteMain.NoteMainActivity;
import com.twago.TaskBook.TaskBook;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

class EditorPresenter implements EditorContract.UserActionListener {
    private static final String TAG = EditorPresenter.class.getSimpleName();
    private int existNoteId;
    private NoteMainActivity activity;
    private MainInterface mainInterface;
    private EditorContract.View noteEditFragmentView;

    private Calendar calendar = Calendar.getInstance();

    EditorPresenter(NoteMainActivity activity, MainInterface mainInterface, EditorContract.View noteEditFragmentView) {
        this.activity = activity;
        this.mainInterface = mainInterface;
        this.noteEditFragmentView = noteEditFragmentView;
        this.existNoteId = noteEditFragmentView.getEditedNoteId();
    }

    @Override
    public void inflateExistNoteData() {
        if (!isNoteNew()) {
            Note existNote = getNoteWithId(existNoteId);
            inflateExistNoteData(existNote);
        }
    }

    private void inflateExistNoteData(Note note) {
        if (note != null) {
            noteEditFragmentView.setTitleNoteEditText(note.getTitle());
            noteEditFragmentView.setTextNoteEditText(note.getText());
            TaskBook.getInstance().setTimeStamp(note.getDate());
        }
    }

    @Override
    public void saveNoteToDatabase() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (isNoteNew()) {
                    int newNoteId = generateNewId(realm);
                    createNewNote(realm, newNoteId);
                }
                else
                    updateExistNote();
            }
        });
    }

    private void setNoteListByCurrentDate() {
        mainInterface.setInfoBarDate();
        mainInterface.showNoteListForDate(isArchiveOpened(), calendar);
    }

    private void createNewNote(Realm realm, int newNoteId) {
        if (!isNoteEmpty()) {
            Note note = new Note(newNoteId, noteEditFragmentView.getTitleNote(),
                    noteEditFragmentView.getTextNote(), TaskBook.getInstance().getTimeStamp(), false);
            realm.copyToRealm(note);
            setNoteListByCurrentDate();
            noteEditFragmentView.notifyItemAdded(newNoteId);
        }
    }

    private int generateNewId(Realm realm) {
        RealmResults<Note> results = realm.where(Note.class).findAll();
        if (results.isEmpty()) return 0;
        return results.max(Note.ID).intValue() + 1;
    }

    private void updateExistNote() {
        Note chosenNote = getNoteWithId(existNoteId);
        chosenNote.setTitle(noteEditFragmentView.getTitleNote());
        chosenNote.setText(noteEditFragmentView.getTextNote());
        chosenNote.setDate(TaskBook.getInstance().getTimeStamp());
        setNoteListByCurrentDate();
    }


    private Note getNoteWithId(int noteId) {
        return Realm.getDefaultInstance().where(Note.class).equalTo(Note.ID, noteId).findFirst();
    }

    private boolean isNoteNew() {
        return existNoteId == Constants.NEW_NOTE_ID;
    }

    private boolean isNoteEmpty() {
        return noteEditFragmentView.getTitleNote().equals("") && noteEditFragmentView.getTextNote().equals("");
    }

    private boolean isArchiveOpened(){
        return existNoteId != Constants.NEW_NOTE_ID;
    }

    @Override
    public void setCurrentNoteDate() {
        calendar.setTimeInMillis(TaskBook.getInstance().getTimeStamp());

        final Note chosenNote = getNoteWithId(existNoteId);
        if (chosenNote != null)
            calendar.setTimeInMillis(chosenNote.getDate());

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        TaskBook.getInstance().setTimeStamp(calendar.getTimeInMillis());
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(activity.getFragmentManager(), "Datepickerdialog");
    }
}
