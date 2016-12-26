package com.twago.note;

/**
 * Created by twago on 24.12.16.
 */

public class Note {

    private int ID;
    private String title;
    private String text;

    public Note(String title, String text, int ID) {
        this.title = title;
        this.text = text;
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getID() {
        return ID;
    }

}
