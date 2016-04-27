package com.example.ishitasinha.paginationapp.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ishitasinha on 22/04/16.
 */
public class ListContract {

    public static final String CONTENT_AUTHORITY = "com.example.ishitasinha.paginationapp.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String TABLE_PATH = "news_items";

    public static final class NewsItems implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_PATH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_PATH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_PATH;

        public static final String TABLE_NAME = "items";
        public static final String COL_ID = "_id";
        public static final String COL_HEADLINE = "headline";
        public static final String COL_IMG_URL = "img_url";
        public static final String COL_AUTHOR = "author";
        public static final String COL_TIMESTAMP = "inserted_at";
        public static final String COL_STORY_ID = "story_id";

        public static Uri buildNewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
