package com.twago.TaskBook.Module;

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
    public static final String DATE = "date";
    public static final String IS_ARCHIVED = "isArchived";
    @NonNull
    private int id;
    @NonNull
    private String title;
    @NonNull
    private String text;
    @NonNull
    private long date;
    @NonNull
    private boolean isArchived;
}
