package com.twago.note;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class NoteMainActivity extends AppCompatActivity{
    FragmentTransaction fragmentTransaction;
    FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);
    }

    public void onCreateNoteClick(View view) {
        /****************** CREATE NEW NOTE *********************************/
        NoteTransaction.isNew = true;
        NoteTransaction.ID = null;  // PASS NULL ID TO NEW NOTE TO NEW FRAGMENT
        /********************************************************************/

        fragmentTransaction = getFragmentManager().beginTransaction();
        MyFragments.noteEditorFragment = new NoteEditorFragment(); // CREATE NEW EDITOR FRAGMENT
        fragmentTransaction.add(R.id.fragmentLayout, MyFragments.noteEditorFragment);

        if(MyFragments.noteListFragment != null) {
            fragmentTransaction.remove(MyFragments.noteListFragment); // DELETE OLD LIST FRAGMENT
            MyFragments.noteListFragment = null;
        }

        fragmentTransaction.commit();
    }

    public void onSaveNoteClick(View view) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(MyFragments.noteEditorFragment);
        MyFragments.noteEditorFragment = null;

        MyFragments.noteListFragment = new NoteListFragment(); // CREATE NEW LIST FRAGMENT
        fragmentTransaction.add(R.id.fragmentLayout, MyFragments.noteListFragment);

        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /******************** CLEAR FRAGMENTS CONTAINER **********************/
        fragmentContainer = (FrameLayout) findViewById(R.id.fragmentLayout);
        fragmentContainer.removeAllViews();
        MyFragments.noteEditorFragment = null;
        MyFragments.noteListFragment = null;

        /******************** CREATE LIST OF NOTES ***************************/
        if (NoteTransaction.notes != null){
            fragmentTransaction = getFragmentManager().beginTransaction();
            MyFragments.noteListFragment = new NoteListFragment(); // CREATE NEW LIST FRAGMENT
            fragmentTransaction.add(R.id.fragmentLayout, MyFragments.noteListFragment);
            fragmentTransaction.commit();
        }
    }
}
