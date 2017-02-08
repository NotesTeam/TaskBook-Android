package com.twago.note.NoteList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twago.note.Constants;
import com.twago.note.Note;
import com.twago.note.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private static final String TAG = ListAdapter.class.getSimpleName();
    private ListContract.UserActionListener userActionListener;
    private RealmResults<Note> noteList;

    ListAdapter(ListContract.UserActionListener userActionListener) {
        this.userActionListener = userActionListener;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.text.setText(note.getText());
        holder.itemView.setBackgroundColor(position % 2 == 0 ? Constants.COLOR_WHITE : Constants.COLOR_GRAY);
        holder.taskIcon.setImageResource(userActionListener.getTaskIcon(note));
        holder.date.setText(userActionListener.getFormatedDate(note));
        setViewHolderOnClickListener(holder, note);
    }

    private void setViewHolderOnClickListener(final ListAdapter.ViewHolder holder, final Note note) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userActionListener.openNewEditor(note.getId());
            }
        });
    }

    public void setData(RealmResults<Note> notes){
        this.noteList = notes;
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
