package com.twago.TaskBook.NoteList;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twago.TaskBook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment implements ListContract.View {

    ListContract.UserActionListener userActionListener;

    public static final String TAG = ListFragment.class.getSimpleName();
    @BindView(R.id.note_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.note_list_day_text)
    TextView dayTextView;
    @BindView(R.id.note_list_month_text)
    TextView monthTextView;


    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        userActionListener = new ListPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        userActionListener.initialization();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public ListAdapter getRecyclerViewAdapter() {
        return (ListAdapter) recyclerView.getAdapter();
    }

    public ListContract.UserActionListener getPresenter() {
        return userActionListener;
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