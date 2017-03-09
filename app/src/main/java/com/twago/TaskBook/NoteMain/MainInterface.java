package com.twago.TaskBook.NoteMain;

/**
 * Created by twago on 22.02.17.
 */

public interface MainInterface {

    void openNewEditor(int id);

    void notifyItemAdded(int id);

    void notifyItemDeleted(int id);
}