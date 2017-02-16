package com.twago.TaskBook.NoteMain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.twago.TaskBook.NoteList.ListFragment;
import com.twago.TaskBook.NoteList.ListPresenter;
import com.twago.TaskBook.R;

import io.realm.Realm;

public class NoteMainActivity extends AppCompatActivity {
    private static final String TAG = NoteMainActivity.class.getSimpleName();
    private ListFragment noteListFragment;
    private MainContract.UserActionListener mainUserActionListener;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pick_date_action:
                choseDate();
                break;
            default:
                noteListFragment.toggleDrawer();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);
        setTitle(R.string.tasks);
        Realm.init(this);

        noteListFragment = ListFragment.newInstance();

        buildMenuButton();
        initSetUp();
    }

    private void buildMenuButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    }

    private void initSetUp() {
        noteListFragment = new ListFragment();
        mainUserActionListener = new MainPresenter(this, new ListPresenter(this, noteListFragment));
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_layout, noteListFragment)
                .commit();
    }

    private void choseDate() {
        mainUserActionListener.setInfoBarDate();
    }
}
