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
    private boolean selectableMode = false;
    private RealmResults<Note> noteList;
    private NoteListAdapterInterface noteListAdapterInterface;
    private ArrayList<ViewHolder> viewHolders;
    private HashMap<Integer,Boolean> selectedNotes = new HashMap<>();

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
                if (!selectableMode)
                    noteListAdapterInterface.openDialogFragment(note.getId());
                else{
                    toggleCheckNote(note.getId(),holder.checkNote);
                    if (!isAnyHolderChecked()){
                        selectableMode = false;
                        hideAllCheckBoxes();
                        noteListAdapterInterface.hideDeleteButton();
                    }
                }
                /*if (!isAnyHolderChecked())
                    noteListAdapterInterface.openDialogFragment(note.getId());
                else {
                    holder.checkNote.setChecked(!holder.checkNote.isChecked());
                    noteListAdapterInterface.toggleCheckNote(note.getId());
                    if (!isAnyHolderChecked()) {
                        hideAllCheckBoxes();
                        noteListAdapterInterface.hideDeleteButton();
                    }
                }*/

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!selectableMode) {
                    selectableMode = true;
                    showAllCheckBoxes();
                    noteListAdapterInterface.showDeleteButton();
                    checkNote(note.getId(),holder.checkNote);
                }
                /*if (!isAnyHolderChecked()) {
                    showAllCheckBoxes();
                    noteListAdapterInterface.showDeleteButton();
                    holder.checkNote.setChecked(true);
                    noteListAdapterInterface.toggleCheckNote(note.getId());
                }*/
                return true;
            }
        });

        viewHolders.add(holder);
    }

    private void checkNote(int id, CheckBox checkBox) {
        noteListAdapterInterface.checkNote(id);
        selectedNotes.put(id,true);
        checkBox.setChecked(true);
    }
    private void uncheckNote(int id, CheckBox checkBox) {
        noteListAdapterInterface.uncheckNote(id);
        selectedNotes.put(id,false);
        checkBox.setChecked(false);
    }

    private void toggleCheckNote(int id, CheckBox checkBox) {
        noteListAdapterInterface.toggleCheckNote(id);
        if (!selectedNotes.containsKey(id))
            selectedNotes.put(id,true);
        else
            selectedNotes.put(id,!selectedNotes.get(id));
        checkBox.toggle();

    }

    private void showAllCheckBoxes() {
        for (ViewHolder holder : viewHolders) {
            holder.checkNote.setVisibility(View.VISIBLE);
        }
    }

    private void hideAllCheckBoxes() {
        for (ViewHolder holder : viewHolders) {
            holder.checkNote.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isAnyHolderChecked(){
        for (Map.Entry<Integer,Boolean> entery : selectedNotes.entrySet()){
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
