package com.twago.note;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import io.realm.Realm;
import io.realm.RealmResults;


public class NoteEditorFragment extends DialogFragment {

    private static final String TAG_ID = "TAG_ID";
    private int noteId;
    LinearLayout editNoteBackground;
    EditText titleNoteEdit;
    EditText textNoteEdit;
    Button saveButton;

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
        final View view = inflater.inflate(R.layout.fragment_note_editor,container,false);
        editNoteBackground = (LinearLayout) view.findViewById(R.id.editNoteBackground);
        titleNoteEdit = (EditText) view.findViewById(R.id.titleEditNote);
        textNoteEdit = (EditText) view.findViewById(R.id.textEditNote);
        saveButton = (Button) view.findViewById(R.id.buttonSave);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
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
        if (note != null){
            titleNoteEdit.setText(note.getTitle());
            textNoteEdit.setText(note.getText());
        }
    }

    private Note getNoteWithId(int noteId) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Note.class).equalTo(Constants.ID,noteId).findFirst(); // Constants.ID
    }

    private void saveNoteToDatabase() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (isNoteNew()) {
                    if (!isNoteEmpty())
                        createNewNote(realm);
                }
                else
                    updateNote(realm);
            }
        });
    }

    private void updateNote(Realm realm) {
        Note note = realm.where(Note.class).equalTo(Constants.ID,noteId).findFirst();
        if (note != null){
            note.setTitle(titleNoteEdit.getText().toString());
            note.setText(textNoteEdit.getText().toString());
        }
    }

    private void createNewNote(Realm realm) {
        Note note = new Note(generateNewId(realm), titleNoteEdit.getText().toString(), textNoteEdit.getText().toString());
        realm.copyToRealm(note);
    }

    private int generateNewId(Realm realm) {
        RealmResults<Note> results = realm.where(Note.class).findAll();
        if (results.isEmpty()) return 0;
        return results.max(Constants.ID).intValue() + 1;
    }

    private boolean isNoteEmpty(){
        return titleNoteEdit.getText().toString().equals("") && textNoteEdit.getText().toString().equals("");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        saveNoteToDatabase();
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof DialogInterface.OnDismissListener){
            ((DialogInterface.OnDismissListener)parentFragment).onDismiss(dialog); // co to?
        }
    }
}

