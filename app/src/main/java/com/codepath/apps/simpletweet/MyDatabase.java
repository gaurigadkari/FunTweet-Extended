package com.codepath.apps.simpletweet;

import com.codepath.apps.simpletweet.models.Tweet;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {

    public static final String NAME = "RestClientDatabase";

    public static final int VERSION = 1;

}
