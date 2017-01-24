package com.twago.note;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import lombok.NonNull;

public class NoteListFragment extends Fragment implements NoteListAdapterInterface, DialogInterface.OnDismissListener {

    public static final String TAG = NoteListFragment.class.getSimpleName();
    @BindView(R.id.button_create_note)
    Button createNote;
    @BindView(R.id.button_delete_note)
    Button deleteNote;
    @BindView(R.id.note_list_recycler_view)
    RecyclerView recyclerView;


    public static NoteListFragment newInstance() {
        Bundle args = new Bundle();

        NoteListFragment fragment = new NoteListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        ButterKnife.bind(this, view);
        inflateRecyclerView();

        return view;
    }

    @OnClick(R.id.button_create_note)
    public void openNewNoteDialogFragment() {
        openDialogFragment(Constants.NEW_NOTE_ID);
    }

    @OnClick(R.id.button_delete_note)
    public void deleteNote() {
        deleteCheckedNotes();
        setVisibilityDeleteButton(false);
        inflateRecyclerView();
    }

    private void inflateRecyclerView() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Note> realmResults = realm.where(Note.class).findAllSorted(Note.TASK, Sort.ASCENDING);

        NoteListAdapter noteListAdapter = new NoteListAdapter(this, realmResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(noteListAdapter);
    }

    private void deleteCheckedNotes() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Note> realmResults = realm.where(Note.class).equalTo("isChecked", true).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.deleteAllFromRealm();
            }
        });

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        inflateRecyclerView();
    }

    @Override
    public void openDialogFragment(int id) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        DialogFragment newFragment = NoteEditorFragment.newInstance(id);
        newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        newFragment.show(fragmentTransaction, "");
    }

    @Override
    public void setVisibilityDeleteButton(boolean mode) {
        createNote.setVisibility(mode ? View.INVISIBLE : View.VISIBLE);
        deleteNote.setVisibility(mode ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void toggleCheckNote(final int noteId) {
        Realm realm = Realm.getDefaultInstance();
        final Note note = realm.where(Note.class).equalTo(Note.ID, noteId).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                note.setChecked(!note.isChecked());
            }
        });
    }

}