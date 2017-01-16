package com.twago.note;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import io.realm.Realm;

public class NoteMainActivity extends AppCompatActivity {
    private static final String TAG = NoteMainActivity.class.getSimpleName();
    private NoteListFragment noteListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);
        Realm.init(this);

        noteListFragment = NoteListFragment.newInstance(); // Dlaczego newInstance zamiast new NoteListFragment?
        initSetUp();
    }

    private void initSetUp(){
        noteListFragment = new NoteListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_layout, noteListFragment)
                .commit();
    }

}
