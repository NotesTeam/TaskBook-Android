package com.twago.note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twago on 25.12.16.
 */

public class NoteTransaction {

    public static Integer ID;
    public static boolean isNew;
    private static List<Integer> allID = new ArrayList<>();
    public static List<Note> notes = new ArrayList<>();


    public static Note getNote(int ID){
        for (Note note : notes){
            if (ID == note.getID()) return note;
        }
        return null;
    }

    public static void setNote(String title, String text, int ID){
        Note note = getNote(ID);
        assert note != null;
        note.setText(text);
        note.setTitle(title);
    }

    public static boolean isIdExist(int ID){
        for (int getID : allID){
            if (getID == ID) return true;
        }
        return false;
    }
}
