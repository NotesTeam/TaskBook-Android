package com.twago.TaskBook.NoteMain;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.twago.TaskBook.Module.Constants;
import com.twago.TaskBook.NoteEditor.EditorContract;
import com.twago.TaskBook.NoteEditor.EditorFragment;
import com.twago.TaskBook.NoteList.ListFragment;
import com.twago.TaskBook.R;
import com.twago.TaskBook.TaskBook;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class NoteMainActivity extends AppCompatActivity implements MainInterface {
    private static final String TAG = NoteMainActivity.class.getSimpleName();
    public static final int NAVIGATION_DRAWER_OPEN = R.string.navigation_drawer_open;
    public static final int NAVIGATION_DRAWER_CLOSE = R.string.navigation_drawer_close;
    private boolean isArchiveOpen;

    private MainContract.UserActionListener mainUserActionListener;
    private ListFragment noteListFragment;
    private EditorContract.View editorFragmentView;
    private ActionBarDrawerToggle toggle;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.drawer_view)
    View drawerView;
    @BindView(R.id.drawer_content)
    View drawerContent;
    @BindView(R.id.main_view)
    View mainView;
    @BindView(R.id.button_create_note)
    FloatingActionButton createNoteButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);
        setTitle(R.string.tasks);
        Realm.init(this);
        ButterKnife.bind(this);

        buildActivity();
        buildListFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initMainPresenter();
        showActiveTaskList();
    }

    private void buildActivity() {
        setSupportActionBar(toolbar);
        buildDrawerLayout();
    }

    private void buildListFragment() {
        noteListFragment = ListFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.main_view, noteListFragment)
                .commit();
    }

    private void initMainPresenter() {
        mainUserActionListener = new MainPresenter(this, noteListFragment.getPresenter(), Realm.getDefaultInstance());
    }

    private void buildDrawerLayout() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, NAVIGATION_DRAWER_OPEN, NAVIGATION_DRAWER_CLOSE);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                drawerContent.setX(drawerView.getWidth() * (1 - slideOffset));
                mainView.setX(drawerView.getWidth() * slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        toggle.syncState();
    }

    private void choseDate(boolean isArchiveOpen) {
        mainUserActionListener.setInfoBarDate(isArchiveOpen);
    }

    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(drawerView))
            drawerLayout.closeDrawer(drawerView);
    }


    @OnClick(R.id.button_create_note)
    public void onCreateNote() {
        noteListFragment.getPresenter().openNewEditor(Constants.NEW_NOTE_ID);
    }

    @OnClick(R.id.show_active_tasks_button)
    public void showActiveTaskList() {
        setTitle(R.string.tasks);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TaskBook.getInstance().getTimeStamp());
        noteListFragment.getPresenter().showNoteListForDate(false, calendar);
        createNoteButton.setVisibility(View.VISIBLE);
        isArchiveOpen = false;
        closeDrawer();
    }

    @OnClick(R.id.show_archive_button)
    public void showArchiveList() {
        setTitle(R.string.archive);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TaskBook.getInstance().getTimeStamp());
        noteListFragment.getPresenter().showNoteListForDate(true, calendar);
        createNoteButton.setVisibility(View.INVISIBLE);
        isArchiveOpen = true;
        closeDrawer();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_pick_date_action)
            choseDate(isArchiveOpen);
        return true;
    }

    @Override
    public void openNewEditor(int id) {
        mainUserActionListener.openNewEditor(id, getFragmentManager());
    }

    @Override
    public void setEditorFragmentView(EditorContract.View editorFragmentView) {
        this.editorFragmentView = editorFragmentView;
    }

    @Override
    public void showNoteListForDate(boolean isArchived, Calendar calendar) {
        noteListFragment.getPresenter().showNoteListForDate(isArchived, calendar);
    }

    @Override
    public void notifyItemAdded(int id) {
        noteListFragment.notifyItemAdded(id);
    }

    @Override
    public void setInfoBarDate() {
        noteListFragment.getPresenter().setCurrentDateInInfoBar();
    }

    @Override
    public void updateNoteColor(int currentColorRes) {
        editorFragmentView.updateNoteColor(currentColorRes);
    }

}

