package com.twago.TaskBook.NoteEditor;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.twago.TaskBook.ColorEditorFragment;
import com.twago.TaskBook.Module.Constants;
import com.twago.TaskBook.Module.Note;
import com.twago.TaskBook.NoteMain.MainInterface;
import com.twago.TaskBook.R;
import com.twago.TaskBook.TaskBook;

import io.realm.Realm;
import io.realm.RealmResults;

class EditorPresenter implements EditorContract.UserActionListener {
    private static final String TAG = EditorPresenter.class.getSimpleName();
    private int existNoteId;
    private int currentColorRes;
    private String currentTask;
    private MainInterface mainInterface;
    private EditorContract.View noteEditFragmentView;
    private Realm realm;

    EditorPresenter(MainInterface mainInterface, EditorContract.View noteEditFragmentView) {
        this.mainInterface = mainInterface;
        this.noteEditFragmentView = noteEditFragmentView;
        this.existNoteId = noteEditFragmentView.getEditedNoteId();
        this.currentColorRes = R.color.transparent_light_gray;
        this.currentTask = Note.MAIN_DAY_TASK;
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    public void inflateExistNoteData() {
        if (!isNoteNew()) {
            Note existNote = getNoteWithId(existNoteId);
            inflateExistNoteData(existNote);
            if (getNoteWithId(existNoteId).isArchived())
                noteEditFragmentView.unableTaskPicker();
        }

        noteEditFragmentView.setTaskView(currentTask);
    }

    @Override
    public void openColorFragment(FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        DialogFragment newFragment = ColorEditorFragment.newInstance(currentColorRes);
        newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SmallScreenDialog);
        newFragment.show(fragmentTransaction, "");
    }

    @Override
    public void saveNoteToDatabase() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (isNoteNew()) {
                    int newNoteId = generateNewId(realm);
                    createNewNote(realm, newNoteId);
                } else {
                    updateExistNote();
                    mainInterface.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void updateNoteColor(int currentColorRes) {
        this.currentColorRes = currentColorRes;
        noteEditFragmentView.setEditorBackgroundColor(currentColorRes);
    }

    @Override
    public void setNoteTask(String currentTask) {
        this.currentTask = currentTask;
    }

    private Note getNoteWithId(int noteId) {
        return Realm.getDefaultInstance().where(Note.class).equalTo(Note.ID, noteId).findFirst();
    }

    private boolean isNoteEmpty() {
        return noteEditFragmentView.getTitleNote().equals("") && noteEditFragmentView.getTextNote().equals("");
    }

    private boolean isNoteNew() {
        return existNoteId == Constants.NEW_NOTE_ID;
    }

    private void createNewNote(Realm realm, int newNoteId) {
        if (!isNoteEmpty()) {
            Note note = new Note(
                    newNoteId,
                    noteEditFragmentView.getTitleNote(),
                    noteEditFragmentView.getTextNote(),
                    TaskBook.getInstance().getTimeStamp(),
                    currentColorRes,
                    currentTask,
                    false);
            realm.copyToRealm(note);
            noteEditFragmentView.notifyItemAdded(newNoteId);
        }
    }

    private int generateNewId(Realm realm) {
        RealmResults<Note> results = realm.where(Note.class).findAll();
        if (results.isEmpty()) return 0;
        return results.max(Note.ID).intValue() + 1;
    }

    private void inflateExistNoteData(Note note) {
        if (note != null) {
            currentColorRes = note.getColorRes();
            currentTask = note.getTask();
            noteEditFragmentView.setTitleNoteEditText(note.getTitle());
            noteEditFragmentView.setTextNoteEditText(note.getText());
            noteEditFragmentView.setEditorBackgroundColor(currentColorRes);
        }
    }

    private void updateExistNote() {
        Note chosenNote = getNoteWithId(existNoteId);
        chosenNote.setTitle(noteEditFragmentView.getTitleNote());
        chosenNote.setText(noteEditFragmentView.getTextNote());
        chosenNote.setDate(TaskBook.getInstance().getTimeStamp());
        chosenNote.setColorRes(currentColorRes);
        chosenNote.setTask(currentTask);
    }
}
