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
import android.widget.LinearLayout;

import com.github.zagum.switchicon.SwitchIconView;
import com.twago.TaskBook.Module.Task;
import com.twago.TaskBook.NoteMain.MainInterface;
import com.twago.TaskBook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;


public class EditorFragment extends DialogFragment implements EditorContract.View {
    private static final String TAG_ID = "TAG_ID";
    private int editedNoteId;
    private MainInterface mainInterface;
    private EditorContract.UserActionListener userActionListener;

    @BindView(R.id.edit_note_background)
    LinearLayout backgroundLayout;
    @BindView(R.id.button_close_note)
    ImageButton closeNoteButton;
    @BindView(R.id.button_set_date)
    ImageButton setDateButton;
    @BindView(R.id.title_edit_note)
    EditText titleNoteEdit;
    @BindView(R.id.text_edit_note)
    EditText textNoteEdit;
    @BindView(R.id.main_task_view)
    SwitchIconView mainTaskView;
    @BindView(R.id.urgent_task_view)
    SwitchIconView urgentTaskView;
    @BindView(R.id.business_task_view)
    SwitchIconView businessTaskView;
    @BindView(R.id.skills_task_view)
    SwitchIconView skillsTaskView;
    @BindView(R.id.buying_task_view)
    SwitchIconView buyingTaskView;

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
        if (context instanceof Activity)
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
        getDialog().getWindow().getAttributes().windowAnimations = R.style.EditorAnimation;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        editedNoteId = getArguments().getInt(TAG_ID);
        userActionListener = new EditorPresenter(mainInterface, this);
        mainInterface.setEditorFragmentView(this);
        setEditorBackgroundColor(R.color.transparent_light_gray);
        userActionListener.inflateExistNoteData();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        userActionListener.saveNoteToDatabase();
        mainInterface.setEditorFragmentView(null);
    }

    @Override
    public int getEditedNoteId() {
        return editedNoteId;
    }

    @Override
    public String getTextNote() {
        return textNoteEdit.getText().toString();
    }

    @Override
    public String getTitleNote() {
        return titleNoteEdit.getText().toString();
    }

    @Override
    public void notifyItemAdded(int id) {
        mainInterface.notifyItemAdded(id);
    }

    @Override
    public void setEditorBackgroundColor(int currentColorRes) {
        backgroundLayout.setBackgroundResource(currentColorRes);
    }

    @Override
    public void setTaskView(Task currentTask) {
        setTaskEnabled(currentTask, false);
    }

    @Override
    public void unableTaskPicker() {
        mainTaskView.setEnabled(false);
        urgentTaskView.setEnabled(false);
        businessTaskView.setEnabled(false);
        skillsTaskView.setEnabled(false);
        buyingTaskView.setEnabled(false);
    }

    @Override
    public void setTextNoteEditText(String text) {
        textNoteEdit.setText(text);
    }

    @Override
    public void setTitleNoteEditText(String title) {
        titleNoteEdit.setText(title);
    }

    @Override
    public void updateNoteColor(int currentColorRes) {
        userActionListener.updateNoteColor(currentColorRes);
    }

    @OnClick(R.id.button_close_note)
    public void closeNote() {
        dismiss();
    }

    @OnClick(R.id.button_set_date)
    public void pickDate() {
        mainInterface.pickDate();
    }

    @OnClick(R.id.button_set_color)
    public void setColor() {
        userActionListener.openColorFragment(getFragmentManager());
    }

    @Optional
    @OnClick({R.id.main_task_view, R.id.urgent_task_view, R.id.business_task_view,
            R.id.skills_task_view, R.id.buying_task_view})
    public void setTask(View view) {
        SwitchIconView switchIconView = (SwitchIconView) view;
        uncheckedAllTasks();
        switchIconView.setIconEnabled(true);
        userActionListener.setNoteTask(getTaskKey(view.getId()));
    }

    private Task getTaskKey(int id) {
        if (id == R.id.main_task_view)
            return Task.MAIN_DAY;
        else if (id == R.id.urgent_task_view)
            return Task.URGENT;
        else if (id == R.id.business_task_view)
            return Task.BUSINESS;
        else if (id == R.id.skills_task_view)
            return Task.SKILL;
        else
            return Task.BUYING;
    }

    private void setTaskEnabled(Task currentTask, boolean anim) {
        switch (currentTask) {
            case MAIN_DAY:
                mainTaskView.setIconEnabled(true, anim);
                break;
            case URGENT:
                urgentTaskView.setIconEnabled(true, anim);
                break;
            case BUSINESS:
                businessTaskView.setIconEnabled(true, anim);
                break;
            case SKILL:
                skillsTaskView.setIconEnabled(true, anim);
                break;
            case BUYING:
                buyingTaskView.setIconEnabled(true, anim);
                break;
        }
    }

    private void uncheckedAllTasks() {
        mainTaskView.setIconEnabled(false);
        urgentTaskView.setIconEnabled(false);
        businessTaskView.setIconEnabled(false);
        skillsTaskView.setIconEnabled(false);
        buyingTaskView.setIconEnabled(false);
    }
}

