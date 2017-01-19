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
    static final String ID = "id";
    static final String TASK = "task";
    static final String MAIN_TASK = "main";
    static final String PART_TASK = "part";
    static final String SKILLS_TASK = "skills";
    static final String UNIMPORTANT_TASK = "unimportant";
    private boolean isChecked = false;
    @NonNull
    private int id;
    @NonNull
    private String title;
    @NonNull
    String text;
    @NonNull
    private String task;
}
