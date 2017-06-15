package com.twago.TaskBook.Module;

import io.realm.RealmObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(suppressConstructorProperties = true)
public class Note extends RealmObject {
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String TASK = "task";
    public static final String IS_ARCHIVED = "isArchived";

    private int id;
    private String title;
    private String text;
    private long date;
    private int colorRes;
    private String task;
    private boolean isArchived;

    public Note(){}

    public Task getTask(){
        return task != null ? Task.valueOf(task) : null;
    }

    public void setTask(Task task){
        this.task = task.name();
    }
}
