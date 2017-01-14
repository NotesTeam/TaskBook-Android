package com.twago.note;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Random;

import io.realm.Realm;


public class NoteEditorFragment extends DialogFragment {

    private static final String TAG_ID = "TAG_ID";
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

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Note note = new Note(titleNoteEdit.getText().toString(),textNoteEdit.getText().toString(),0);
                realm.copyToRealm(note);
            }
        });
    }

    public void createNote(){
        /****** GET DATA FROM THE FRAGMENT ********/
        String title = titleNoteEdit.getText().toString();
        String text = textNoteEdit.getText().toString();

            if (NoteConfigurations.isNew) {
                if (!(title.equals("") && text.equals(""))) {
                    Random random = new Random();
                    int randomID;
                    do {
                        randomID = random.nextInt(1000);
                    }
                    while (NoteConfigurations.isIdExist(randomID));

                    NoteConfigurations.ID = randomID;
                    Note newNote = new Note(title, text, NoteConfigurations.ID);
                    NoteConfigurations.addNote(newNote);
                }
            }
            else {
                if (title.equals("") && text.equals(""))
                    NoteConfigurations.deleteNote(NoteConfigurations.ID);
                else
                    NoteConfigurations.setNote(title, text, NoteConfigurations.ID);
            }

    }
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editNoteBackground.getWindowToken(), 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        /*if(FragmentOptions.isRotated){
            FragmentOptions.editorSavedTitle = titleNoteEdit.getText().toString();
            FragmentOptions.editorSavedText = textNoteEdit.getText().toString();
        }*/
    }
}

