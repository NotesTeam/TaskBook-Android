package com.twago.note;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.twago.note.NoteEditor.EditorContract;
import com.twago.note.NoteEditor.EditorFragment;
import com.twago.note.NoteList.ListContract;
import com.twago.note.NoteList.ListFragment;
import com.twago.note.NoteList.ListPresenter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import io.realm.Realm;

public class NoteMainActivity extends AppCompatActivity {
    private static final String TAG = NoteMainActivity.class.getSimpleName();
    private ListFragment noteListFragment;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes_list,menu);
        return true;
    }

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

    public void choseDate(MenuItem item) {
    }
}
