package com.twago.note;

interface NoteListAdapterInterface {
    void openDialogFragment(int id);
    void showDeleteButton();
    void hideDeleteButton();
    void toggleCheckNote(int noteId);
}
