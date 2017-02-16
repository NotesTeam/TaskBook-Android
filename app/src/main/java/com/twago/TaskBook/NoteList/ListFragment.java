package com.twago.TaskBook.NoteList;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.twago.TaskBook.Module.Constants;
import com.twago.TaskBook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListFragment extends Fragment implements ListContract.View {

    ListContract.UserActionListener userActionListener;

    public static final String TAG = ListFragment.class.getSimpleName();
    @BindView(R.id.button_create_note)
    ImageButton createNote;
    @BindView(R.id.note_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.note_list_day_text)
    TextView dayTextView;
    @BindView(R.id.note_list_month_text)
    TextView monthTextView;
    Drawer drawer;


    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userActionListener = new ListPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        ButterKnife.bind(this, view);
        buildDrawer();
        setupRecyclerView();
        userActionListener.initialization();

        return view;
    }

    private void buildDrawer() {
        drawer = new DrawerBuilder()
                .withActivity(getActivity())
                .withTranslucentNavigationBar(false)
                .addDrawerItems(
                        buildActiveTasksDrawerItem(),
                        buildArchiveDrawerItem()
                )
                .withSliderBackgroundColorRes(R.color.dark_blue)
                .withDrawerWidthDp(60)
                .build();
    }

    private PrimaryDrawerItem buildActiveTasksDrawerItem() {
        return new PrimaryDrawerItem()
                .withIcon(R.drawable.ic_date_range_white_36dp)
                .withName(R.string.tasks)
                .withSelectedColorRes(R.color.granate)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        userActionListener.openActiveTasks();
                        getActivity().setTitle(R.string.tasks);
                        createNote.setVisibility(View.VISIBLE);
                        return false;
                    }
                });
    }

    private PrimaryDrawerItem buildArchiveDrawerItem() {
        return new PrimaryDrawerItem()
                .withIcon(R.drawable.ic_archive_white_36dp)
                .withName(R.string.archive)
                .withSelectedColorRes(R.color.granate)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        userActionListener.openArchive();
                        getActivity().setTitle(R.string.archive);
                        createNote.setVisibility(View.INVISIBLE);
                        return false;
                    }
                });
    }

    public void toggleDrawer() {
        if (drawer.isDrawerOpen())
            drawer.closeDrawer();
        else
            drawer.openDrawer();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @OnClick(R.id.button_create_note)
    public void openNewNoteDialogFragment() {
        userActionListener.openNewEditor(Constants.NEW_NOTE_ID);
    }

    @Override
    public ListAdapter getRecyclerViewAdapter() {
        return (ListAdapter) recyclerView.getAdapter();
    }

    @Override
    public void setAdapterOnRecyclerView(ListAdapter listAdapter) {
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void setDateInInfoBar(String dayText, String monthText) {
        dayTextView.setText(dayText);
        monthTextView.setText(monthText);
    }
}