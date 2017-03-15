package com.twago.TaskBook.NoteList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twago.TaskBook.Module.Note;
import com.twago.TaskBook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import ru.rambler.libs.swipe_layout.SwipeLayout;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private static final String TAG = ListAdapter.class.getSimpleName();
    private ListContract.UserActionListener userActionListener;
    private RealmList<Note> noteList;

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
        Log.d(TAG, "onBindViewHolder");
        final Note note = noteList.get(position);
        inflateRecyclerView(holder, note);
    }

    @Override
    public int getItemCount() {
        return noteList == null ? 0 :noteList.size();
    }

    public void addElement(Note note){
        noteList.add(0,note);
    }

    public void setData(RealmList<Note> notes) {
        this.noteList = notes;
    }

    private void inflateCenterSwipeView(ViewHolder holder, Note note) {
        holder.centerSwipeView.setBackgroundResource(note.getColorRes());
        holder.title.setText(note.getTitle());
        holder.text.setText(note.getText());
    }

    private void inflateRecyclerView(ViewHolder holder, Note note) {
        inflateCenterSwipeView(holder, note);
        inflateRightSwipeView(holder, note);
        setSwipeLayoutListener(holder, note);
        setCenterSwipeViewOnClickListener(holder, note);
    }

    private void inflateRightSwipeView(ViewHolder holder, Note note) {
        if (note.isArchived())
            setSwipeViewDeleteStyle(holder);
        else
            setSwipeViewArchiveStyle(holder);
    }

    private void removeItemFromRecyclerView(ViewHolder holder) {
        noteList.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
    }

    private void setCenterSwipeViewOnClickListener(final ListAdapter.ViewHolder holder, final Note note) {
        holder.centerSwipeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userActionListener.openNewEditor(note.getId());
            }
        });
    }

    private void setSwipeLayoutListener(final ViewHolder holder, final Note note) {
        holder.swipeLayout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onBeginSwipe(SwipeLayout swipeLayout, boolean moveToRight) {}

            @Override
            public void onSwipeClampReached(SwipeLayout swipeLayout, boolean moveToRight) {
                if (!moveToRight) {
                    if (note.isArchived())
                        userActionListener.deleteNote(note.getId());
                    else
                        userActionListener.archiveNote(note.getId());
                    removeItemFromRecyclerView(holder);
                    swipeLayout.reset();
                }
            }

            @Override
            public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {}

            @Override
            public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {}
        });
    }

    private void setSwipeViewArchiveStyle(ViewHolder holder) {
        holder.rightSwipeView.setBackgroundResource(R.color.archive_green);
        holder.deleteIcon.setBackgroundResource(R.drawable.ic_archive_white_36dp);
    }

    private void setSwipeViewDeleteStyle(ViewHolder holder) {
        holder.rightSwipeView.setBackgroundResource(R.color.delete_red);
        holder.deleteIcon.setBackgroundResource(R.drawable.ic_delete_white_36dp);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_list_row_bottom_note_center)
        View centerSwipeView;
        @BindView(R.id.note_list_row_bottom_swipe_right)
        View rightSwipeView;
        @BindView(R.id.note_list_row_swipe_layout)
        SwipeLayout swipeLayout;
        @BindView(R.id.note_list_row_title)
        TextView title;
        @BindView(R.id.note_list_row_text)
        TextView text;
        @BindView(R.id.note_list_row_right_swipe_icon)
        ImageView deleteIcon;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
