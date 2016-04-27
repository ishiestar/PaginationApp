package com.example.ishitasinha.paginationapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;

public class ListProvider extends ContentProvider {

    private DbHelper mDbHelper;

    public ListProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mDbHelper
                .getWritableDatabase()
                .delete(
                        uri.getLastPathSegment(),
                        selection,
                        selectionArgs
                );
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long _id = mDbHelper
                .getWritableDatabase()
                .insert(
                        ListContract.NewsItems.TABLE_NAME,
                        null,
                        values
                );
        if ( _id > 0 )
            return ListContract.NewsItems.buildNewsUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return mDbHelper
                .getReadableDatabase()
                .query(
                        ListContract.NewsItems.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return mDbHelper
                .getWritableDatabase()
                .update(
                        uri.getLastPathSegment(),
                        values,
                        selection,
                        selectionArgs
                );
    }
}
