package com.twago.note.NoteList;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.twago.note.Constants;
import com.twago.note.Note;
import com.twago.note.NoteEditor.EditorFragment;
import com.twago.note.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class ListFragment extends Fragment implements DialogInterface.OnDismissListener, ListContract.View {

    ListContract.UserActionListener userActionListener;

    public static final String TAG = ListFragment.class.getSimpleName();
    @BindView(R.id.button_create_note)
    Button createNote;
    @BindView(R.id.button_delete_note)
    Button deleteNote;
    @BindView(R.id.note_list_recycler_view)
    RecyclerView recyclerView;


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
        userActionListener.inflateView();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @OnClick(R.id.button_create_note)
    public void openNewNoteDialogFragment() {
        openNoteEditor(Constants.NEW_NOTE_ID);
    }

    @OnClick(R.id.button_delete_note)
    public void deleteNote() {
        userActionListener.deleteCheckedNotes();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        userActionListener.inflateView();
    }

    @Override
    public void setDeleteButtonVisibility(int visibility) {
        deleteNote.setVisibility(visibility);
    }

    @Override
    public void setCreateButtonVisibility(int visibility) {
        createNote.setVisibility(visibility);
    }

    @Override
    public void setAdapterOnRecyclerViewFromDB(ListAdapter listAdapter) {
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void openNoteEditor(int id) {
        userActionListener.openNewEditor(id,getChildFragmentManager());
    }
}