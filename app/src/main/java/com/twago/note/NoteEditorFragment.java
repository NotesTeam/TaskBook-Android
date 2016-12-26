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

        /************************** INITIAL VIEWS IN THE FRAGMENT *******************/
        editNoteBackground = (LinearLayout) v.findViewById(R.id.editNoteBackground);
        titleNoteEdit = (EditText) v.findViewById(R.id.titleEditNote);
        textNoteEdit = (EditText) v.findViewById(R.id.textEditNote);
        backButton = (Button) v.findViewById(R.id.buttonBack);
        /****************************************************************************/


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFragments.fragmentTransaction = getFragmentManager().beginTransaction();
                MyFragments.fragmentTransaction.remove(MyFragments.noteEditorFragment);
                MyFragments.noteEditorFragment = null;

                MyFragments.noteListFragment = new NoteListFragment(); // CREATE NEW LIST FRAGMENT
                MyFragments.fragmentTransaction.add(R.id.fragmentLayout, MyFragments.noteListFragment);

                MyFragments.fragmentTransaction.commit();
            }
        });


        /************ PUT DATA FROM EXIST NOTE TO FRAGMENT (IF IS EXIST) ************/
        if (!NoteTransaction.isNew){
            Note noteToEdit = NoteTransaction.getNote(NoteTransaction.ID);
            assert noteToEdit != null;
            titleNoteEdit.setText(noteToEdit.getTitle());
            textNoteEdit.setText(noteToEdit.getText());
        }
        /****************************************************************************/

        return v;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        backButton.setVisibility(View.INVISIBLE);


        /****** GET DATA FROM THE FRAGMENT ********/
        String title = titleNoteEdit.getText().toString();
        String text = textNoteEdit.getText().toString();

        /******************* IF NOTE ISN'T EMPTY ************************/
        if (!(title.equals("") && text.equals(""))) {

            /***** IF THE NOTE IS NEW THEN CREATE NEW NOTE **/
            if (NoteTransaction.isNew) {
                Random random = new Random();
                int randomID;
                do {
                    randomID = random.nextInt(1000);
                }
                while (NoteTransaction.isIdExist(randomID));

                NoteTransaction.ID = randomID;
                Note newNote = new Note(title, text, NoteTransaction.ID);
                NoteTransaction.notes.add(newNote);
            }

            /***** IF THE NOTE IS EXIST THEN UPDATE THE NOTE ***/
            else {
                NoteTransaction.setNote(title, text, NoteTransaction.ID);
            }
        }

        MyFragments.noteEditorFragment = null; // DELETE THE FRAGMENT
    }
}

