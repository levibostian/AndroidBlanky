package com.levibostian.androidblanky.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.structure.BaseModel;

@com.raizlabs.android.dbflow.annotation.Table(database = Database.class)
public class Table extends BaseModel {

    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name;

}
