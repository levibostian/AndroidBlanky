package com.levibostian.androidblanky.db.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SampleModel extends RealmObject {

    @PrimaryKey public long id;
    @Required public Date created;
    @Required public String title;
    public boolean done;
    public OtherSampleModel other_model;

    public SampleModel() {
    }

}