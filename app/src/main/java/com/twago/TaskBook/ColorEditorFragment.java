package com.twago.TaskBook;



import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twago.TaskBook.NoteMain.MainInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class ColorEditorFragment extends DialogFragment {
    private static final String COLOR_ID = "COLOR_ID";
    int currentColorRes;
    private MainInterface mainInterface;

    @BindView(R.id.button_color_gray)
    AppCompatButton buttonColorGray;
    @BindView(R.id.button_color_blue)
    AppCompatButton buttonColorBlue;
    @BindView(R.id.button_color_pink)
    AppCompatButton buttonColorPink;
    @BindView(R.id.button_color_ochra)
    AppCompatButton buttonColorOchra;

    public static ColorEditorFragment newInstance(int currentColorRes) {
        Bundle args = new Bundle();
        args.putInt(COLOR_ID,currentColorRes);
        ColorEditorFragment fragment = new ColorEditorFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_color_editor, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        currentColorRes = getArguments().getInt(COLOR_ID);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mainInterface.updateNoteColor(currentColorRes);
    }

    @Optional
    @OnClick({ R.id.button_color_gray, R.id.button_color_blue,
            R.id.button_color_pink, R.id.button_color_ochra })
    public void setCurrentColorRes(View view){
        switch (view.getId()){
            case R.id.button_color_gray :
                Log.wtf("WTF","gray");
                currentColorRes = R.color.transparent_light_gray;
                break;
            case R.id.button_color_blue :
                Log.wtf("WTF","gray");
                currentColorRes = R.color.transparent_light_blue;
                break;
            case R.id.button_color_pink :
                Log.wtf("WTF","gray");
                currentColorRes = R.color.transparent_light_pink;
                break;
            case R.id.button_color_ochra :
                Log.wtf("WTF","gray");
                currentColorRes = R.color.transparent_ochra;
                break;
        }
        dismiss();
    }
}
