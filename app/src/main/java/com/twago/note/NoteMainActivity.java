package com.twago.note;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;

public class NoteMainActivity extends AppCompatActivity {
    private static final String TAG = NoteMainActivity.class.getSimpleName();
    private NoteListFragment noteListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);
        Realm.init(this);
        logDataBaseContent();

        noteListFragment = NoteListFragment.newInstance();
        initSetUp();
    }

    private void logDataBaseContent() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Note> results = realm.where(Note.class).findAll();
        Log.d(TAG,results.toString());
    }

    private void initSetUp(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        noteListFragment = new NoteListFragment();
        fragmentTransaction.add(R.id.fragmentLayout, noteListFragment);
        fragmentTransaction.commit();
    }

}
