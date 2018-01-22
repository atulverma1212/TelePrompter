package com.example.averma1212.teleprompter.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by HP on 09-01-2018.
 */

public class ScriptContract {

    public static final String AUTHORITY = "com.example.averma1212.teleprompter";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "scripts";



    public static final class ScriptEntry implements BaseColumns {

        public static final String TABLE_NAME = "scripts";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
    }
}
