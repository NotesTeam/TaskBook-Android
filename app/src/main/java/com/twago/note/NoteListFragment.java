package com.twago.note;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment {

    private LinearLayout noteListLayout; // LIST OF NOTE LAYOUT (VERTICAL)
    private List<Button> noteViews; // LIST OF NOTE VIEWS
    private Button addNote;
    private Button deleteNote;
    private Button cancelNote;
    private View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_note_list, container, false);

        noteListLayout = (LinearLayout) v.findViewById(R.id.noteList);
        addNote = (Button) v.findViewById(R.id.buttonAdd);
        deleteNote = (Button) v.findViewById(R.id.buttonDelete);
        cancelNote = (Button) v.findViewById(R.id.buttonCancel);
        noteViews = new ArrayList<>();


        /********************************************************************************/

        createListOfNotes(); // CREATE LIST OF NOTES

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNote();
            }
        });

        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNote();
            }
        });

        cancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        return v;
    }

    private void createListOfNotes(){

        /*********************** PARAMS FOR VIEW OF NOTE ****************************/
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20,20,20,20);
        /****************************************************************************/

        /*********************** CREATE LIST OF NOTES ******************************/
        NoteConfigurations.reverseNoteList(); // REVERSE LIST FOR CHANGE ORDER OF LIST OF NOTES

        for (Note note : NoteConfigurations.getNoteList()){
            Button noteButton = new Button(getActivity());
            noteButton.setText(note.getTitle());
            noteButton.setPadding(30,30,30,30);
            noteButton.setTextSize(18);
            noteButton.setId(note.getID());
            noteButton.setGravity(Gravity.LEFT);
            noteButton.setTextColor(getResources().getColor(R.color.dark_gray));

            noteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /************** EDIT EXIST NOTE ********/
                    System.out.println(view.getId());
                    NoteConfigurations.ID = view.getId();
                    NoteConfigurations.isNew = false;

                    FragmentOptions.fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentOptions.noteEditorFragment = new NoteEditorFragment();
                    FragmentOptions.fragmentTransaction.add(R.id.fragmentLayout, FragmentOptions.noteEditorFragment);
                    FragmentOptions.fragmentTransaction.remove(FragmentOptions.noteListFragment);
                    FragmentOptions.noteListFragment = null;
                    FragmentOptions.fragmentTransaction.commit();
                    /***************************************/
                }
            });

            noteButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    deleteNote.setVisibility(View.VISIBLE);
                    cancelNote.setVisibility(View.VISIBLE);
                    addNote.setVisibility(View.INVISIBLE);
                    NoteConfigurations.setCheckedView(view);
                    NoteConfigurations.ID_TO_DELETE = view.getId();
                    System.out.println(NoteConfigurations.ID_TO_DELETE);
                    freezeNotes();
                    return true;
                }
            });
            noteViews.add(noteButton); // ADD NOTE VIEW TO LIST
            noteListLayout.addView(noteButton, layoutParams); // ADD NOTE VIEW TO NOTE LIST LAYOUT

        }
        NoteConfigurations.reverseNoteList(); // BACK REVERSE
        /**************************************************************************/
    }
    private void createNote(){
        /****************** CREATE NEW NOTE *********************************/
        NoteConfigurations.isNew = true;
        NoteConfigurations.ID = null;  // PASS NULL ID TO NEW NOTE TO NEW FRAGMENT
        /********************************************************************/

        FragmentOptions.fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentOptions.noteEditorFragment = new NoteEditorFragment(); // CREATE NEW EDITOR FRAGMENT
        FragmentOptions.fragmentTransaction.add(R.id.fragmentLayout, FragmentOptions.noteEditorFragment);

        if(FragmentOptions.noteListFragment != null) {
            FragmentOptions.fragmentTransaction.remove(FragmentOptions.noteListFragment); // DELETE OLD LIST FRAGMENT
            FragmentOptions.noteListFragment = null;
        }

        FragmentOptions.fragmentTransaction.commit();
    }
    private void deleteNote(){
        NoteConfigurations.deleteNote(NoteConfigurations.ID_TO_DELETE);
        deleteNote.setVisibility(View.INVISIBLE);
        cancelNote.setVisibility(View.INVISIBLE);
        View checkedView = NoteConfigurations.getCheckedView();

        FragmentOptions.fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentOptions.noteListFragment = new NoteListFragment();
        FragmentOptions.fragmentTransaction.replace(R.id.fragmentLayout,FragmentOptions.noteListFragment);
        FragmentOptions.fragmentTransaction.commit();
        unfreezeNotes();
        NoteConfigurations.ID_TO_DELETE = null;
    }
    private void cancel(){
        Button view = (Button) v.findViewById(NoteConfigurations.ID_TO_DELETE);
        System.out.println(view.getId());
        deleteNote.setVisibility(View.INVISIBLE);
        cancelNote.setVisibility(View.INVISIBLE);
        addNote.setVisibility(View.VISIBLE);
        NoteConfigurations.ID_TO_DELETE = null;
        unfreezeNotes();
    }

    private void freezeNotes(){
        for (int i = 0; i < noteViews.size(); i++){
            noteViews.get(i).setEnabled(false);
        }
    }
    private void unfreezeNotes(){
        for (int i = 0; i < noteViews.size(); i++){
            noteViews.get(i).setEnabled(true);
        }
    }


}