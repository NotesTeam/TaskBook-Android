package com.twago.note;

interface NoteListAdapterInterface {
    void openDialogFragment(int id);
    void setVisibilityDeleteButton(boolean mode);
    void toggleCheckNote(int noteId);
}
