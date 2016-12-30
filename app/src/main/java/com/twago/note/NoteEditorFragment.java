package com.twago.note;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditorFragment extends Fragment {

    LinearLayout editNoteBackground;
    EditText titleNoteEdit;
    EditText textNoteEdit;
    Button saveButton;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_note_editor,container,false);
        editNoteBackground = (LinearLayout) v.findViewById(R.id.editNoteBackground);
        titleNoteEdit = (EditText) v.findViewById(R.id.titleEditNote);
        textNoteEdit = (EditText) v.findViewById(R.id.textEditNote);
        saveButton = (Button) v.findViewById(R.id.buttonSave);

        /******************* PUT SAVED DATA AFTER ROTATIONS *************************/
        if(FragmentOptions.isRotated){
            titleNoteEdit.setText(FragmentOptions.editorSavedTitle);
            textNoteEdit.setText(FragmentOptions.editorSavedText);
            FragmentOptions.isRotated = false;
        }

        /************ PUT DATA FROM EXIST NOTE TO FRAGMENT (IF IS EXIST) ************/
        if (!NoteConfigurations.isNew){
            Note noteToEdit = NoteConfigurations.getNote(NoteConfigurations.ID);
            assert noteToEdit != null;
            titleNoteEdit.setText(noteToEdit.getTitle());
            textNoteEdit.setText(noteToEdit.getText());
        }
        /****************************************************************************/

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNote();
                hideKeyboard();
                FragmentOptions.fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentOptions.fragmentTransaction.remove(FragmentOptions.noteEditorFragment);
                FragmentOptions.noteEditorFragment = null;
                FragmentOptions.noteListFragment = new NoteListFragment(); // CREATE NEW LIST FRAGMENT
                FragmentOptions.fragmentTransaction.add(R.id.fragmentLayout, FragmentOptions.noteListFragment);
                FragmentOptions.fragmentTransaction.commit();

            }
        });

        /****************************************************************************/

        return v;
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
        if(FragmentOptions.isRotated){
            FragmentOptions.editorSavedTitle = titleNoteEdit.getText().toString();
            FragmentOptions.editorSavedText = textNoteEdit.getText().toString();
        }
    }
}

