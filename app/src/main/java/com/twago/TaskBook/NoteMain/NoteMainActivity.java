package com.twago.TaskBook.NoteMain;

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
import android.widget.TextView;

import com.twago.TaskBook.Module.Constants;
import com.twago.TaskBook.NoteEditor.EditorContract;
import com.twago.TaskBook.NoteList.ListFragment;
import com.twago.TaskBook.R;
import com.twago.TaskBook.TaskBook;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class NoteMainActivity extends AppCompatActivity implements MainInterface, MainContract.View {
    private static final String TAG = NoteMainActivity.class.getSimpleName();
    public static final int NAVIGATION_DRAWER_OPEN = R.string.navigation_drawer_open;
    public static final int NAVIGATION_DRAWER_CLOSE = R.string.navigation_drawer_close;
    private boolean isArchiveOpen;

    private EditorContract.View editorFragmentView;
    private MainContract.UserActionListener mainUserActionListener;
    private ListFragment noteListFragment;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.button_create_note)
    FloatingActionButton createNoteButton;
    @BindView(R.id.drawer_content)
    View drawerContent;
    @BindView(R.id.drawer_view)
    View drawerView;
    @BindView(R.id.main_view)
    View mainView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.note_list_infobar_day_text)
    TextView dayInfoBarTextView;
    @BindView(R.id.note_list_infobar_month_text)
    TextView monthInfoBarTextView;


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
        setDateInInfoBar();
        showActiveTaskList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_pick_date_action)
            pickDate();
        return true;
    }

    @Override
    public void notifyDataSetChanged() {
        noteListFragment.notifyDataSetChanged();
    }

    @Override
    public void notifyItemAdded(int id) {
        noteListFragment.notifyItemAdded(id);
    }

    @Override
    public void openNewEditor(int id) {
        mainUserActionListener.openNewEditor(id, getFragmentManager());
    }

    @Override
    public void pickDate() {
        mainUserActionListener.setDate(isArchiveOpen, getFragmentManager());
    }

    @Override
    public void setDateInInfoBar(String dayText, String monthText) {
        dayInfoBarTextView.setText(dayText);
        monthInfoBarTextView.setText(monthText);
    }

    @Override
    public void setEditorFragmentView(EditorContract.View editorFragmentView) {
        this.editorFragmentView = editorFragmentView;
    }

    @Override
    public void updateNoteColor(int currentColorRes) {
        editorFragmentView.updateNoteColor(currentColorRes);
    }

    @Override
    public void updateRecyclerView(boolean isArchiveOpen, Calendar calendar) {
        noteListFragment.updateRecyclerView(isArchiveOpen,calendar);
    }

    @OnClick(R.id.button_create_note)
    public void onCreateNote() {
        noteListFragment.openNewEditor(Constants.NEW_NOTE_ID);
    }

    @OnClick(R.id.show_active_tasks_button)
    public void showActiveTaskList() {
        setTitle(R.string.tasks);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TaskBook.getInstance().getTimeStamp());
        noteListFragment.updateRecyclerView(false, calendar);
        createNoteButton.setVisibility(View.VISIBLE);
        isArchiveOpen = false;
        closeDrawer();
    }

    @OnClick(R.id.show_archive_button)
    public void showArchiveList() {
        setTitle(R.string.archive);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TaskBook.getInstance().getTimeStamp());
        noteListFragment.updateRecyclerView(true, calendar);
        createNoteButton.setVisibility(View.INVISIBLE);
        isArchiveOpen = true;
        closeDrawer();
    }

    private void buildActivity() {
        setSupportActionBar(toolbar);
        buildDrawerLayout();
    }

    private void buildDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, NAVIGATION_DRAWER_OPEN, NAVIGATION_DRAWER_CLOSE);
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

    private void buildListFragment() {
        noteListFragment = ListFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.main_view, noteListFragment)
                .commit();
    }

    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(drawerView))
            drawerLayout.closeDrawer(drawerView);
    }

    private void initMainPresenter() {
        mainUserActionListener = new MainPresenter(this);
    }

    private void setDateInInfoBar() {
        mainUserActionListener.setDateInInfoBar();
    }

}

