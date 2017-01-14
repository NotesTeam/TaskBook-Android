package com.twago.note;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class NoteListFragment extends Fragment {

    private LinearLayout noteListLayout; // LIST OF NOTE LAYOUT (VERTICAL)
    private List<Button> noteViews; // LIST OF NOTE VIEWS
    private NoteMainActivity activity;
    private Button createNote;
    private Button cancelNote;
    private Button colorNote;
    private Button deleteNote;
    private View view;
    private RecyclerView recyclerView;

    public static NoteListFragment newInstance() {

        Bundle args = new Bundle();

        NoteListFragment fragment = new NoteListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof NoteMainActivity)
            activity = (NoteMainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_note_list, container, false);


        noteListLayout = (LinearLayout) view.findViewById(R.id.noteList);
        createNote = (Button) view.findViewById(R.id.buttonCreate);
        deleteNote = (Button) view.findViewById(R.id.buttonDelete);
        cancelNote = (Button) view.findViewById(R.id.buttonCancel);
        colorNote = (Button) view.findViewById(R.id.buttonColor);
        recyclerView = (RecyclerView) view.findViewById(R.id.note_list_recycler_view);
        noteViews = new ArrayList<>();

        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.openDialogFragment(Constants.NEW_NOTE_ID);
            }
        });

        /*deleteNote.setOnClickListener(new View.OnClickListener() {
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
        });*/

        /*colorNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ColorActivity.class);
                startActivity(intent);
            }
        });*/


        inflateRecyclerView();
        return view;
    }

    private void inflateRecyclerView() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Note> results = realm.where(Note.class).findAll();
        NoteListAdapter noteListAdapter = new NoteListAdapter(activity,results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(noteListAdapter);
    }


    /*private void createListOfNotes() {

        *//*********************** PARAMS FOR VIEW OF NOTE ****************************//*
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);
        *//****************************************************************************//*

        *//*********************** CREATE LIST OF NOTES ******************************//*
        NoteConfigurations.reverseNoteList(); // REVERSE LIST FOR CHANGE ORDER OF LIST OF NOTES

        for (Note note : NoteConfigurations.getNoteList()) {
            Button noteButton = new Button(getActivity());
            noteButton.setText(note.getTitle());
            noteButton.setPadding(30, 30, 30, 30);
            noteButton.setTextSize(16);
            noteButton.setTypeface(Typeface.DEFAULT_BOLD);
            noteButton.setId(note.getID());
            noteButton.setGravity(Gravity.LEFT);
            noteButton.setTextColor(getResources().getColor(R.color.dark_gray));

            noteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    NoteConfigurations.ID = view.getId();
                    NoteConfigurations.isNew = false;

                    fragmentTransaction = getFragmentManager().beginTransaction();
                    FragmentOptions.noteEditorFragment = new NoteEditorFragment();
                    FragmentOptions.fragmentTransaction.add(R.id.fragmentLayout, FragmentOptions.noteEditorFragment);
                    FragmentOptions.fragmentTransaction.remove(FragmentOptions.noteListFragment);
                    FragmentOptions.noteListFragment = null;
                    FragmentOptions.fragmentTransaction.commit();
                    *//***************************************//*
                }
            });

            noteButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    deleteNote.setVisibility(View.VISIBLE);
                    cancelNote.setVisibility(View.VISIBLE);
                    createNote.setVisibility(View.INVISIBLE);
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
        *//**************************************************************************//*
    }*/

    /*private void createNote() {
        *//****************** CREATE NEW NOTE *********************************//*
        NoteConfigurations.isNew = true;
        NoteConfigurations.ID = null;  // PASS NULL ID TO NEW NOTE TO NEW FRAGMENT
        *//********************************************************************//*

        FragmentOptions.fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentOptions.noteEditorFragment = new NoteEditorFragment(); // CREATE NEW EDITOR FRAGMENT
        FragmentOptions.fragmentTransaction.add(R.id.fragmentLayout, FragmentOptions.noteEditorFragment);

        if (FragmentOptions.noteListFragment != null) {
            FragmentOptions.fragmentTransaction.remove(FragmentOptions.noteListFragment); // DELETE OLD LIST FRAGMENT
            FragmentOptions.noteListFragment = null;
        }

        FragmentOptions.fragmentTransaction.commit();
    }

    private void deleteNote() {
        NoteConfigurations.deleteNote(NoteConfigurations.ID_TO_DELETE);
        deleteNote.setVisibility(View.INVISIBLE);
        cancelNote.setVisibility(View.INVISIBLE);
        View checkedView = NoteConfigurations.getCheckedView();

        FragmentOptions.fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentOptions.noteListFragment = new NoteListFragment();
        FragmentOptions.fragmentTransaction.replace(R.id.fragmentLayout, FragmentOptions.noteListFragment);
        FragmentOptions.fragmentTransaction.commit();
        unfreezeNotes();
        NoteConfigurations.ID_TO_DELETE = null;
    }

    private void cancel() {
        Button view = (Button) this.view.findViewById(NoteConfigurations.ID_TO_DELETE);
        System.out.println(view.getId());
        deleteNote.setVisibility(View.INVISIBLE);
        cancelNote.setVisibility(View.INVISIBLE);
        createNote.setVisibility(View.VISIBLE);
        NoteConfigurations.ID_TO_DELETE = null;
        unfreezeNotes();
    }

    private void freezeNotes() {
        for (int i = 0; i < noteViews.size(); i++) {
            noteViews.get(i).setEnabled(false);
        }
    }

    private void unfreezeNotes() {
        for (int i = 0; i < noteViews.size(); i++) {
            noteViews.get(i).setEnabled(true);
        }
    }*/


}