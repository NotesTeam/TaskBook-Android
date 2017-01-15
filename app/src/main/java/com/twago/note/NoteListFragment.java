package com.twago.note;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class NoteListFragment extends Fragment implements NoteListAdapterInterface, DialogInterface.OnDismissListener {

    private LinearLayout noteListLayout; // LIST OF NOTE LAYOUT (VERTICAL)
    private List<Button> noteViews; // LIST OF NOTE VIEWS
    private NoteMainActivity activity;
    private Button createNote;
    private Button cancelNote;
    private Button colorNote;
    private Button deleteNote;
    private View view;
    private RecyclerView recyclerView;
    private NoteListAdapter noteListAdapter;

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
                openDialogFragment(Constants.NEW_NOTE_ID);
            }
        });

        inflateRecyclerView();
        return view;
    }

    private void inflateRecyclerView() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Note> results = realm.where(Note.class).findAll();
        noteListAdapter = new NoteListAdapter(this,results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(noteListAdapter);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        Log.d(NoteListFragment.class.getSimpleName(),"DialogDismiss");
        inflateRecyclerView();
    }

    @Override
    public void openDialogFragment(int id) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        DialogFragment newFragment = NoteEditorFragment.newInstance(id);
        newFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.FullScreenDialog);
        newFragment.show(ft, "");
    }

}