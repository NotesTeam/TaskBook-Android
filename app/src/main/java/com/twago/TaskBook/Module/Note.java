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
    public static final String TASK = "task";

    public static final String MAIN_DAY_TASK = "main_day_task";
    public static final String URGENT_TASK = "urgent_task";
    public static final String BUSINESS_TASK = "business_task";
    public static final String SKILL_TASK = "skill_task";
    public static final String BUYING_TASK = "buying_task";

    @NonNull
    private int id;
    @NonNull
    private String title;
    @NonNull
    private String text;
    @NonNull
    private long date;
    @NonNull
    private int colorRes;
    @NonNull
    private String task;
    @NonNull
    private boolean isArchived;
}
