package com.twago.note;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
    private static final int COLOR_GRAY = Color.parseColor("#efefef");
    private static final int COLOR_WHITE = Color.parseColor("#FFFFFF");
    private static final String TAG = NoteListAdapter.class.getSimpleName();
    private ArrayList<Note> noteArrayList;
    private NoteListAdapterInterface noteListAdapterInterface;

    NoteListAdapter(NoteListAdapterInterface noteListAdapterInterface,ArrayList<Note> noteArrayList) {
        this.noteListAdapterInterface = noteListAdapterInterface;
        this.noteArrayList = noteArrayList;
    }

    @Override
    public NoteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NoteListAdapter.ViewHolder holder, final int position) {
        final Note note = noteArrayList.get(position);
        holder.title.setText(note.getTitle());
        holder.text.setText(note.getText());
        holder.itemView.setBackgroundColor(position % 2 == 0 ? COLOR_WHITE : COLOR_GRAY);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,String.format("item clicked: %d",note.getID()));
                noteListAdapterInterface.openDialogFragment(note.getID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.note_list_row_title);
            text = (TextView) itemView.findViewById(R.id.note_list_row_text);
        }
    }
}
