package com.twago.note;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    Button backButton;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note_editor,container,false);
        editNoteBackground = (LinearLayout) v.findViewById(R.id.editNoteBackground);
        titleNoteEdit = (EditText) v.findViewById(R.id.titleEditNote);
        textNoteEdit = (EditText) v.findViewById(R.id.textEditNote);
        backButton = (Button) v.findViewById(R.id.buttonSave);
        /****************************************************************************/

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNote();
                FragmentOptions.fragmentTransaction = getFragmentManager().beginTransaction();
                FragmentOptions.fragmentTransaction.remove(FragmentOptions.noteEditorFragment);
                FragmentOptions.noteEditorFragment = null;
                FragmentOptions.noteListFragment = new NoteListFragment(); // CREATE NEW LIST FRAGMENT
                FragmentOptions.fragmentTransaction.add(R.id.fragmentLayout, FragmentOptions.noteListFragment);

                FragmentOptions.fragmentTransaction.commit();
            }
        });


        /************ PUT DATA FROM EXIST NOTE TO FRAGMENT (IF IS EXIST) ************/
        if (!NoteConfigurations.isNew){
            Note noteToEdit = NoteConfigurations.getNote(NoteConfigurations.ID);
            assert noteToEdit != null;
            titleNoteEdit.setText(noteToEdit.getTitle());
            textNoteEdit.setText(noteToEdit.getText());
        }
        /****************************************************************************/

        return v;
    }

    public void createNote(){
        /****** GET DATA FROM THE FRAGMENT ********/
        String title = titleNoteEdit.getText().toString();
        String text = textNoteEdit.getText().toString();

        /******************* IF NOTE ISN'T EMPTY ************************/
        if (!(title.equals("") && text.equals(""))) {

            /***** IF THE NOTE IS NEW THEN CREATE NEW NOTE **/
            if (NoteConfigurations.isNew) {
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

            /***** IF THE NOTE IS EXIST THEN UPDATE THE NOTE ***/
            else {
                NoteConfigurations.setNote(title, text, NoteConfigurations.ID);
            }
        }
    }
}

