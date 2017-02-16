package com.twago.TaskBook.NoteEditor;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.twago.TaskBook.NoteMain.NoteMainActivity;
import com.twago.TaskBook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditorFragment extends DialogFragment implements EditorContract.View {
    private static final String TAG = EditorFragment.class.getSimpleName();
    private static final String TAG_ID = "TAG_ID";
    private int chosenNoteId;
    private EditorContract.UserActionListener userActionListener;

    @BindView(R.id.button_close_note)
    ImageButton closeNoteButton;
    @BindView(R.id.button_set_date)
    ImageView setDateButton;
    @BindView(R.id.title_edit_note)
    EditText titleNoteEdit;
    @BindView(R.id.text_edit_note)
    EditText textNoteEdit;

    public static EditorFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(TAG_ID, id);
        EditorFragment fragment = new EditorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_note_editor, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        chosenNoteId = getArguments().getInt(TAG_ID);
        userActionListener = new EditorPresenter((NoteMainActivity) getActivity(), this);
        userActionListener.inflateChosenNoteData();
    }

    @OnClick(R.id.button_set_date)
    public void pickDate() {
        userActionListener.setCurrentNoteDate();
    }

    @OnClick(R.id.button_close_note)
    public void closeNote() {
        dismiss();
    }

    @Override
    public void blockArchivedNoteViews() {
        titleNoteEdit.setHint("");
        textNoteEdit.setHint("");
        titleNoteEdit.setEnabled(false);
        textNoteEdit.setEnabled(false);
        setDateButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getChosenNoteId() {
        return chosenNoteId;
    }

    @Override
    public void setTitleNoteEditText(String title) {
        titleNoteEdit.setText(title);
    }

    @Override
    public void setTextNoteEditText(String text) {
        textNoteEdit.setText(text);
    }

    @Override
    public String getTitleNote() {
        return titleNoteEdit.getText().toString();
    }

    @Override
    public String getTextNote() {
        return textNoteEdit.getText().toString();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        userActionListener.saveNoteToDatabase();
    }
}

