package com.twago.note;

import io.realm.RealmObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class Note extends RealmObject {
    public static final String ID = "id";
    public static final String TASK = "task";
    public static final String DATE = "date";
    private boolean isChecked = false;
    @NonNull
    private int id;
    @NonNull
    private String title;
    @NonNull
    private String text;
    @NonNull
    private String task;
    @NonNull
    private long date;
}
