package com.twago.note.NoteList;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.twago.note.Constants;
import com.twago.note.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListFragment extends Fragment implements DialogInterface.OnDismissListener, ListContract.View {

    ListContract.UserActionListener userActionListener;

    public static final String TAG = ListFragment.class.getSimpleName();
    @BindView(R.id.button_create_note)
    ImageButton createNote;
    @BindView(R.id.note_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.note_list_day_text)
    TextView dayText;
    @BindView(R.id.note_list_month_text)
    TextView monthText;


    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userActionListener = new ListPresenter(getActivity(),this);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        userActionListener.setDate(dayText,monthText);
        userActionListener.inflateView();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @OnClick(R.id.button_create_note)
    public void openNewNoteDialogFragment() {
        userActionListener.openNewEditor(Constants.NEW_NOTE_ID);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        userActionListener.inflateView();
    }

    @Override
    public void setAdapterOnRecyclerViewFromDB(ListAdapter listAdapter) {
        recyclerView.setAdapter(listAdapter);
    }
}