package com.twago.note;

import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by twago on 25.12.16.
 */

class NoteConfigurations {
    private static List<Integer> allID = new ArrayList<>();
    private static List<Note> notes = new ArrayList<>();
    private static View checkedView;

    static Integer ID;
    static Integer ID_TO_DELETE;
    static boolean isNew;



    static List<Note> getNoteList(){
        return notes;
    }

    static Note getNote(int ID){
        for (Note note : notes){
            if (ID == note.getId()) return note;
        }
        return null;
    }

    static void addNote(Note note){
        notes.add(note);
    }

    static void setNote(String title, String text, int ID){
        Note note = getNote(ID);
        assert note != null;
        note.setText(text);
        note.setTitle(title);
    }

    static boolean isIdExist(int ID){
        for (int getID : allID){
            if (getID == ID) return true;
        }
        return false;
    }

    static void deleteNote(int ID){
        notes.remove(getNote(ID));
    }

    static void reverseNoteList(){
        Collections.reverse(notes);
    }

    static View getCheckedView() {
        return checkedView;
    }

    static void setCheckedView(View checkedView) {
        checkedView = checkedView;
    }
}
