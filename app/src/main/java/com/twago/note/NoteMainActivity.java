package com.twago.note;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class NoteMainActivity extends AppCompatActivity{
    FrameLayout fragmentContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);

        /******************** CLEAR FRAGMENTS CONTAINER **********************/
        fragmentContainer = (FrameLayout) findViewById(R.id.fragmentLayout);
        fragmentContainer.removeAllViews();
        MyFragments.noteEditorFragment = null;
        MyFragments.noteListFragment = null;

        /******************** CREATE LIST OF NOTES ***************************/
            MyFragments.fragmentTransaction = getFragmentManager().beginTransaction();
            MyFragments.noteListFragment = new NoteListFragment(); // CREATE NEW LIST FRAGMENT
            MyFragments.fragmentTransaction.add(R.id.fragmentLayout, MyFragments.noteListFragment);
            MyFragments.fragmentTransaction.commit();
    }
}
