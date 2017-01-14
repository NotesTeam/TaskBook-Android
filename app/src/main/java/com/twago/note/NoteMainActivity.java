package com.twago.note;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoteMainActivity extends AppCompatActivity implements NoteListAdapterInterface{
    private NoteListFragment noteListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);

        noteListFragment = NoteListFragment.newInstance();
        initSetUp();
    }

    private void initSetUp(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        noteListFragment = new NoteListFragment();
        fragmentTransaction.add(R.id.fragmentLayout, noteListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void openDialogFragment(int id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = NoteEditorFragment.newInstance(id);
        newFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.FullScreenDialog);
        newFragment.show(ft, "");
    }
}
