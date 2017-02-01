package com.twago.note;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twago.note.NoteList.ListFragment;

import io.realm.Realm;

public class NoteMainActivity extends AppCompatActivity {
    private static final String TAG = NoteMainActivity.class.getSimpleName();
    private ListFragment noteListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);
        Realm.init(this);

        noteListFragment = ListFragment.newInstance();
        initSetUp();
    }

    private void initSetUp(){
        noteListFragment = new ListFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_layout, noteListFragment)
                .commit();
    }

}
