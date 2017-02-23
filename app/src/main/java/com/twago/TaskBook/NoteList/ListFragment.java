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
import android.widget.TextView;

import com.twago.TaskBook.Module.Note;
import com.twago.TaskBook.NoteMain.MainInterface;
import com.twago.TaskBook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ListFragment extends Fragment implements ListContract.View {

    ListContract.UserActionListener userActionListener;

    public static final String TAG = ListFragment.class.getSimpleName();
    @BindView(R.id.note_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.note_list_infobar_day_text)
    TextView dayTextView;
    @BindView(R.id.note_list_infobar_month_text)
    TextView monthTextView;
    private MainInterface mainInterface;

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
        userActionListener = new ListPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        userActionListener.inflateListFragment();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public ListContract.UserActionListener getPresenter() {
        return userActionListener;
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

    @Override
    public void openNewEditor(int id) {
        mainInterface.openNewEditor(id);
    }

    public void notifyItemAdded(int id) {
        Note note = Realm.getDefaultInstance().where(Note.class).equalTo(Note.ID, id).findFirst();
        getRecyclerViewAdapter().addElement(note);
        getRecyclerViewAdapter().notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
    }
}