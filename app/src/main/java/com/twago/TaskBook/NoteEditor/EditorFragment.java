package com.twago.TaskBook.NoteEditor;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twago.TaskBook.NoteMain.MainInterface;
import com.twago.TaskBook.NoteMain.NoteMainActivity;
import com.twago.TaskBook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditorFragment extends DialogFragment implements EditorContract.View {
    private static final String TAG = EditorFragment.class.getSimpleName();
    private static final String TAG_ID = "TAG_ID";
    private int editedNoteId;
    private MainInterface mainInterface;
    private EditorContract.UserActionListener userActionListener;

    @BindView(R.id.edit_note_background)
    LinearLayout backgroundLayout;
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

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_note_editor, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        editedNoteId = getArguments().getInt(TAG_ID);
        initEditorPresenter();
        setEditorBackgroundColor(R.color.transparent_light_gray);

        mainInterface.setEditorFragmentView(this);
        userActionListener.inflateExistNoteData();
    }

    private void initEditorPresenter() {
        userActionListener = new EditorPresenter((NoteMainActivity) getActivity(), mainInterface, this);
    }

    @OnClick(R.id.button_set_date)
    public void pickDate() {
        mainInterface.choseDate();
    }

    @OnClick(R.id.button_set_color)
    public void setColor() {
        userActionListener.openColorFragment(getFragmentManager());
    }

    @OnClick(R.id.button_close_note)
    public void closeNote() {
        dismiss();
    }

    @Override
    public int getEditedNoteId() {
        return editedNoteId;
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
    public void notifyItemAdded(int id) {
        mainInterface.notifyItemAdded(id);
    }

    @Override
    public void updateNoteColor(int currentColorRes) {
        userActionListener.updateNoteColor(currentColorRes);
    }

    @Override
    public void setEditorBackgroundColor(int currentColorRes) {
        backgroundLayout.setBackgroundResource(currentColorRes);
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
        mainInterface.setEditorFragmentView(null);
    }
}

