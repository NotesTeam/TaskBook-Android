package com.twago.note;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class NoteMainActivity extends AppCompatActivity{
    FrameLayout fragmentContainer;
    boolean isRotated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);

        createNoteListFragment();
    }

    private void createNoteListFragment(){
        FragmentOptions.fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentOptions.noteListFragment = new NoteListFragment();
        FragmentOptions.fragmentTransaction.add(R.id.fragmentLayout, FragmentOptions.noteListFragment);
        FragmentOptions.fragmentTransaction.commit();
    }
    private void createNoteEditorFragment(){
        FragmentOptions.fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentOptions.noteEditorFragment = new NoteEditorFragment();
        FragmentOptions.fragmentTransaction.add(R.id.fragmentLayout, FragmentOptions.noteEditorFragment);
        FragmentOptions.fragmentTransaction.commit();
    }

}
