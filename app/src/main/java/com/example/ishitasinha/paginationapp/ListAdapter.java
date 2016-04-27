package com.example.ishitasinha.paginationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ishitasinha.paginationapp.provider.ListContract;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ishitasinha on 22/04/16.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    List<ListItem> items;
    Context context;

    ListAdapter(List<ListItem> items) {
        this.items = items;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        ListItem item = items.get(position);
        holder.headline.setText(item.getHeadline());
        holder.author.setText(item.getAuthor());
        if (item.getPosterUrl() != null) {
            holder.image.setBackgroundColor(Color.TRANSPARENT);
            Picasso.with(context).load(item.getPosterUrl()).into(holder.image);
        }
        String storyId = (item.getId() == null) ? "" + item.getHeadline().hashCode() : item.getId();
        Cursor cursor = context
                .getContentResolver()
                .query(
                        ListContract.NewsItems.CONTENT_URI,
                        new String[]{ListContract.NewsItems.COL_ID},
                        ListContract.NewsItems.COL_STORY_ID + "=?",
                        new String[]{storyId},
                        null
                );
        if(!cursor.moveToFirst()) {
            ContentValues cv = new ContentValues();
            cv.put(ListContract.NewsItems.COL_STORY_ID, storyId);
            cv.put(ListContract.NewsItems.COL_AUTHOR, item.getAuthor());
            cv.put(ListContract.NewsItems.COL_HEADLINE, item.getHeadline());
            cv.put(ListContract.NewsItems.COL_IMG_URL, item.getPosterUrl());
            Uri uri = context.getContentResolver().insert(ListContract.NewsItems.CONTENT_URI, cv);
            Log.i(ListAdapter.class.getSimpleName(), "Inserted values at: " + uri);
        }
        cursor.close();
    }

    public void addAll(List<ListItem> newItems) {
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView headline;
        TextView author;

        public ListViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            headline = (TextView) itemView.findViewById(R.id.headline);
            author = (TextView) itemView.findViewById(R.id.author);
        }
    }
}
