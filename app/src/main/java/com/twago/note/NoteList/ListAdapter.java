package com.twago.note.NoteList;

import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twago.note.Constants;
import com.twago.note.Note;
import com.twago.note.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import butterknife.BindView;
import lombok.Getter;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private static final String TAG = ListAdapter.class.getSimpleName();
    private ListContract.UserActionListener userActionListener;
    private RealmResults<Note> noteList;
    private FragmentManager fragmentManager;

    //TODO delete
    @Getter
    private boolean checkableMode = false;
    private HashMap<Integer, Boolean> checkedNotesList = new HashMap<>();

    ListAdapter(ListContract.UserActionListener userActionListener, RealmResults<Note> noteList, FragmentManager fragmentManager) {
        this.userActionListener = userActionListener;
        this.noteList = noteList;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        final Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.text.setText(note.getText());
        holder.itemView.setBackgroundColor(position % 2 == 0 ? Constants.COLOR_WHITE : Constants.COLOR_GRAY);
        holder.taskIcon.setImageResource(getTaskIcon(note));
        holder.date.setText(getFormatedDate(note));

        setOnClickListener(holder, note);
        setOnLongClickListener(holder, note);
    }

    private void setOnLongClickListener(final ViewHolder holder, final Note note) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!checkableMode) {
                    setCheckableMode(true);
                    toggleCheckNote(note, holder.itemView);
                }
                return true;
            }
        });
    }

    private void setOnClickListener(final ViewHolder holder, final Note note) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkableMode)
                    toggleCheckNote(note, holder.itemView);
                else
                userActionListener.openNewEditor(note.getId(),fragmentManager);
                if (!isAnyHolderChecked())
                    setCheckableMode(false);
            }
        });
    }

    private String getFormatedDate(Note note) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd MMM yyyy",Locale.getDefault());
        return simpleDateFormat.format(new Date(note.getDate()));
    }

    private void setCheckableMode(boolean mode) {
        checkableMode = mode;
        //listAdapterInterface.setVisibilityDeleteButton(mode);
    }

    private void toggleCheckNote(Note note, View view) {
        int id = note.getId();
        userActionListener.toggleCheckNoteInDB(id);
        if (!checkedNotesList.containsKey(id))
            checkedNotesList.put(id, true);
        else
            checkedNotesList.put(id, !checkedNotesList.get(id));
        toggleItemView(view, note);
    }

    private void toggleItemView(View view, Note note) {
        if (note.isChecked())
            view.setBackgroundColor(Constants.COLOR_DARK_GRAY);
        else
            view.setBackgroundColor(Constants.COLOR_WHITE);
    }

    private boolean isAnyHolderChecked() {
        for (Map.Entry<Integer, Boolean> entery : checkedNotesList.entrySet()) {
            if (entery.getValue()) return true;
        }
        return false;
    }

    private int getTaskIcon(Note note){
        switch (note.getTask()){
            case Constants.MAIN_TASK :
                return R.drawable.ic_star_indigo_500_24dp;
            case Constants.PART_TASK :
                return R.drawable.ic_star_half_indigo_500_24dp;
            case Constants.SKILLS_TASK :
                return R.drawable.ic_lightbulb_outline_indigo_500_24dp;
            case Constants.UNIMPORTANT_TASK :
                return R.drawable.ic_help_outline_indigo_500_24dp;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_list_row_title)
        TextView title;
        @BindView(R.id.note_list_row_text)
        TextView text;
        @BindView(R.id.note_list_row_task_icon)
        ImageView taskIcon;
        @BindView(R.id.note_list_row_date_text)
        TextView date;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
