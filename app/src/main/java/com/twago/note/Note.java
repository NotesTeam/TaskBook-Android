package com.twago.note;

import io.realm.RealmObject;

public class Note extends RealmObject {
    static final String ID = "id";
    private int id;
    private boolean isChecked;
    private String title;
    private String text;

    public Note(){}

    public Note(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
        isChecked = false;
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

    public int getId() {
        return id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
