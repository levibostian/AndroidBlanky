package com.levibostian.androidblanky.db.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class OtherSampleModel extends RealmObject {

    @PrimaryKey public long id;
    @Required public String first_name;
    @Required public String last_name;
    @Required public String username;

    public OtherSampleModel() {
    }

    public String getFirstAndLastName() {
        return first_name.concat(" ").concat(last_name);
    }

}