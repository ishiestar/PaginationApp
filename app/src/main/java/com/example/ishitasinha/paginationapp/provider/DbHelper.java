package com.example.ishitasinha.paginationapp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

/**
 * Created by ishitasinha on 22/04/16.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "news_provider";

    public static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        db.delete(
                ListContract.NewsItems.TABLE_NAME,
                ListContract.NewsItems.COL_TIMESTAMP + "<?",
                new String[]{"" + cal.getTimeInMillis()}
        );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_NEWS_TABLE = "CREATE TABLE " +
                ListContract.NewsItems.TABLE_NAME + "( " +
                ListContract.NewsItems.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ListContract.NewsItems.COL_HEADLINE + " TEXT, " +
                ListContract.NewsItems.COL_AUTHOR + " TEXT, " +
                ListContract.NewsItems.COL_IMG_URL + " TEXT, " +
                ListContract.NewsItems.COL_TIMESTAMP + " DATETIME  DEFAULT CURRENT_TIMESTAMP, " +
                ListContract.NewsItems.COL_STORY_ID + " TEXT" +
                " );";

        sqLiteDatabase.execSQL(CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
