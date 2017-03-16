package com.twago.TaskBook.NoteList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twago.TaskBook.Module.Note;
import com.twago.TaskBook.NoteMain.MainInterface;
import com.twago.TaskBook.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ListFragment extends Fragment implements ListContract.View {
    public static final String TAG = ListFragment.class.getSimpleName();

    private ListContract.UserActionListener userActionListener;
    private MainInterface mainInterface;

    @BindView(R.id.note_list_recycler_view)
    RecyclerView noteListRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity)
            mainInterface = (MainInterface) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainInterface = (MainInterface) activity;
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userActionListener = new ListPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        userActionListener.inflateListFragment();

        return view;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return noteListRecyclerView;
    }

    @Override
    public ListAdapter getRecyclerViewAdapter() {
        return (ListAdapter) noteListRecyclerView.getAdapter();
    }

    @Override
    public void openNewEditor(int id) {
        mainInterface.openNewEditor(id);
    }

    @Override
    public void setRecyclerViewAdapter(ListAdapter listAdapter) {
        noteListRecyclerView.setAdapter(listAdapter);
    }

    public void notifyDataSetChanged() {
        getRecyclerViewAdapter().notifyDataSetChanged();
    }

    public void notifyItemAdded(int id) {
        Note note = Realm.getDefaultInstance().where(Note.class).equalTo(Note.ID, id).findFirst();
        getRecyclerViewAdapter().addElement(note);
        getRecyclerViewAdapter().notifyItemInserted(0);
        getRecyclerView().scrollToPosition(0);
    }

    public void updateRecyclerView(boolean isArchived, Calendar calendar) {
        userActionListener.updateRecyclerView(isArchived,calendar);
    }

    private void setupRecyclerView() {
        noteListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}