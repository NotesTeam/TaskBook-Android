package com.twago.TaskBook.NoteEditor;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.twago.TaskBook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditorFragment extends DialogFragment implements EditorContract.View {
    private static final String TAG_ID = "TAG_ID";
    private static final String TAG = EditorFragment.class.getSimpleName();
    private int noteId;
    EditorContract.UserActionListener userActionListener;

    @BindView(R.id.button_close_note)
    ImageButton closeNoteButton;
    @BindView(R.id.button_set_date)
    ImageView setDateButton;
    @BindView(R.id.title_edit_note)
    EditText titleNoteEdit;
    @BindView(R.id.text_edit_note)
    EditText textNoteEdit;
    @BindView(R.id.main_task_note_editor)
    ImageView mainTaskButton;
    @BindView(R.id.part_task_note_editor)
    ImageView partTaskButton;
    @BindView(R.id.skills_task_note_editor)
    ImageView skillsTaskButton;
    @BindView(R.id.unimportant_task_note_editor)
    ImageView unimportantTaskButton;

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_note_editor, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        noteId = getArguments().getInt(TAG_ID);
        userActionListener = new EditorPresenter(getActivity(), this);
        userActionListener.inflateOldData();
    }

    @OnClick(R.id.button_close_note)
    public void closeNote() {
        dismiss();
    }

    @OnClick({R.id.main_task_note_editor, R.id.part_task_note_editor, R.id.skills_task_note_editor, R.id.unimportant_task_note_editor})
    public void pickTypeTaskNote(ImageView taskIcon) {
        userActionListener.pickTaskNote(taskIcon);
    }

    @OnClick(R.id.button_set_date)
    public void pickDate() {
        userActionListener.pickDate(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        userActionListener.saveNoteToDatabase();
    }

    @Override
    public int getNoteId() {
        return noteId;
    }

    @Override
    public EditText getTitleNoteEditText() {
        return titleNoteEdit;
    }

    @Override
    public EditText getTextNoteEditText() {
        return textNoteEdit;
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
    public String getTitleNote() {
        return titleNoteEdit.getText().toString();
    }

    @Override
    public String getTextNote() {
        return textNoteEdit.getText().toString();
    }
}

