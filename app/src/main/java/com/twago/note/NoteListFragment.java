package com.twago.note;


import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment {
    private LinearLayout noteListLayout; // LIST OF NOTE LAYOUT (VERTICAL)
    private List<Button> noteViews; // LIST OF NOTE VIEWS
    private FragmentTransaction fragmentTransaction;
    private LinearLayout.LayoutParams layoutParams; // PARAMS FOR VIEW OF NOTE

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note_list, container, false);

        noteListLayout = (LinearLayout) v.findViewById(R.id.noteList);
        noteViews = new ArrayList<>();
        fragmentTransaction = getFragmentManager().beginTransaction();

        /*********************** CREATE LIST OF NOTES ********************/
        createListOfNotes();

        return v;
    }

    private void createListOfNotes(){

        /*********************** PARAMS FOR VIEW OF NOTE ****************************/
        layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20,20,20,20);
        /****************************************************************************/


        /*********************** CREATE LIST OF NOTES ******************************/
        Collections.reverse(NoteTransaction.notes); // REVERSE LIST FOR CHANGE ORDER OF LIST OF NOTES

        for (Note note : NoteTransaction.notes){
            Button noteButton = new Button(getActivity());
            noteButton.setText(note.getTitle());
            noteButton.setPadding(20,20,20,20);
            noteButton.setTextSize(30);
            noteButton.setId(note.getID());

            noteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /************** EDIT EXIST NOTE ********/
                    System.out.println(view.getId());
                    NoteTransaction.ID = view.getId();
                    NoteTransaction.isNew = false;

                    MyFragments.noteEditorFragment = new NoteEditorFragment();
                    fragmentTransaction.add(R.id.fragmentLayout, MyFragments.noteEditorFragment);
                    fragmentTransaction.remove(MyFragments.noteListFragment);
                    fragmentTransaction.commit();
                    /***************************************/
                }
            });
            noteViews.add(noteButton); // ADD NOTE VIEW TO LIST
            noteListLayout.addView(noteButton,layoutParams); // ADD NOTE VIEW TO NOTE LIST LAYOUT

        }

        Collections.reverse(NoteTransaction.notes); // BACK REVERSE
        /**************************************************************************/
    }

}