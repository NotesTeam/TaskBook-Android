package com.twago.note;

import io.realm.RealmObject;

public class Note extends RealmObject {
    private int id;
    private String title;
    private String text;

    public Note(){}

    public Note(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
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

}
