package com.twago.note;

import io.realm.RealmObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public @AllArgsConstructor @NoArgsConstructor
class Note extends RealmObject {
    static final String ID = "id";
    private @Getter @Setter boolean isChecked = false;
    private @Getter int id;
    private @Getter @Setter String title;
    private @Getter @Setter String text;
}
