package com.twago.note;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import lombok.Getter;

class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
    private static final String TAG = NoteListAdapter.class.getSimpleName();
    @Getter
    private boolean checkableMode = false;
    private RealmResults<Note> noteList;
    private NoteListAdapterInterface noteListAdapterInterface;
    private ArrayList<ViewHolder> viewHolders;
    private HashMap<Integer, Boolean> checkedNotesList = new HashMap<>();

    NoteListAdapter(NoteListAdapterInterface noteListAdapterInterface, RealmResults<Note> noteList) {
        this.noteListAdapterInterface = noteListAdapterInterface;
        this.noteList = noteList;
        viewHolders = new ArrayList<>();
    }

    @Override
    public NoteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NoteListAdapter.ViewHolder holder, final int position) {
        final Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.text.setText(note.getText());
        holder.itemView.setBackgroundColor(position % 2 == 0 ? Constants.COLOR_WHITE : Constants.COLOR_GRAY);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkableMode)
                    noteListAdapterInterface.openDialogFragment(note.getId());
                else {
                    toggleCheckNote(note.getId(), holder.checkNote);
                    if (!isAnyHolderChecked())
                        setCheckableMode(Constants.START_MODE);
                }

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!checkableMode) {
                    setCheckableMode(Constants.END_MODE);
                    toggleCheckNote(note.getId(), holder.checkNote);
                }
                return true;
            }
        });
        viewHolders.add(holder);
    }

    private void setCheckableMode(boolean mode) {
        if (mode) {
            checkableMode = true;
            setVisibilityRecyclerListCheckBoxes(Constants.VISIBLE);
            noteListAdapterInterface.showDeleteButton();
        } else {
            checkableMode = false;
            setVisibilityRecyclerListCheckBoxes(Constants.INVISIBLE);
            noteListAdapterInterface.hideDeleteButton();
        }
    }

    private void setVisibilityRecyclerListCheckBoxes(boolean visibility) {
        if (visibility == Constants.VISIBLE) {
            for (ViewHolder holder : viewHolders)
                holder.checkNote.setVisibility(View.VISIBLE);
        } else {
            for (ViewHolder holder : viewHolders)
                holder.checkNote.setVisibility(View.INVISIBLE);
        }
    }

    private void toggleCheckNote(int id, CheckBox checkBox) {
        noteListAdapterInterface.toggleCheckNote(id);
        if (!checkedNotesList.containsKey(id))
            checkedNotesList.put(id, true);
        else
            checkedNotesList.put(id, !checkedNotesList.get(id));
        checkBox.toggle();

    }

    private boolean isAnyHolderChecked() {
        for (Map.Entry<Integer, Boolean> entery : checkedNotesList.entrySet()) {
            if (entery.getValue()) return true;
        }
        return false;
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
        @BindView(R.id.note_list_row_check_note)
        CheckBox checkNote;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
