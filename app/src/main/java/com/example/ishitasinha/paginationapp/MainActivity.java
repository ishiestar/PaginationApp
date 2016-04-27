package com.example.ishitasinha.paginationapp;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ishitasinha.paginationapp.provider.ListContract;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private int previousTotal = 0;
    private boolean isLoading = true;
    private int visibleThreshold = 10;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    LinearLayoutManager manager;
    RecyclerView recyclerView;
    ListView listView;
    ListAdapter rvAdapter;
    SimpleCursorAdapter lvAdapter;
    SwipeRefreshLayout refreshLayout;
    ApiService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.lv_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_widget);
        refreshLayout.setOnRefreshListener(this);

        checkInternet();
        Log.v("onCreate: internet", isInternetActive ? "active" : "not active");

        if (isInternetActive) {
            fetchData();
        } else {
            showCachedData();
        }
    }

    private void fetchData() {
        Log.v(LOG_TAG, "fetchData called");
        listView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        refreshLayout.setRefreshing(true);
        service = ApiSingleton.getService();
        previousTotal = 0;
        service.getItem("10", "0", "id,headline,author-name,hero-image-metadata")
                .enqueue(new Callback<List<ListItem>>() {
                    @Override
                    public void onResponse(Call<List<ListItem>> call, Response<List<ListItem>> response) {
                        rvAdapter = new ListAdapter(response.body());
                        recyclerView.setAdapter(rvAdapter);
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<ListItem>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Failed to get data.", Toast.LENGTH_SHORT).show();
                        refreshLayout.setRefreshing(false);
                    }
                });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = manager.getItemCount();
                firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (isLoading) {
                    if (totalItemCount > previousTotal) {
                        isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    service.getItem("10", "" + (firstVisibleItem + visibleThreshold), "headline,author-name,hero-image-metadata").enqueue(new Callback<List<ListItem>>() {
                        @Override
                        public void onResponse(Call<List<ListItem>> call, Response<List<ListItem>> response) {
                            List<ListItem> newItems = response.body();
                            rvAdapter.addAll(newItems);
                        }

                        @Override
                        public void onFailure(Call<List<ListItem>> call, Throwable t) {

                        }
                    });
                    isLoading = true;
                }
            }
        });
    }

    private void showCachedData() {
        Log.v(LOG_TAG, "showCachedData called");
        listView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        lvAdapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.list_item,
                null,
                new String[]{
                        ListContract.NewsItems.COL_HEADLINE,
                        ListContract.NewsItems.COL_AUTHOR
                },
                new int[]{
                        R.id.headline,
                        R.id.author
                },
                0);
        getSupportLoaderManager().initLoader(0, null, this);
        listView.setAdapter(lvAdapter);
    }

    Boolean isInternetActive = false;

    public void setInternetActive(Boolean internetActive) {
        isInternetActive = internetActive;
        if(!internetActive) Toast.makeText(this, "Internet is not connected.", Toast.LENGTH_SHORT).show();
    }

    private void checkInternet() {
        if (isNetworkAvailable()) {
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... voids) {
                    try {
                        HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                        urlc.setRequestProperty("User-Agent", "Test");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(1500);
                        urlc.connect();
                        return (urlc.getResponseCode() == 200);
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error checking internet connection", e);
                    }
                    return false;
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    super.onPostExecute(result);
                    setInternetActive(result);
                    if(result)
                        fetchData();
                    Log.v("AsyncThread", "internet " + (isInternetActive ? "active" : "not active"));
                }
            }.execute();

        } else {
            Log.d(LOG_TAG, "No network available!");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getApplicationContext(),
                ListContract.NewsItems.CONTENT_URI,
                new String[]{
                        ListContract.NewsItems.COL_ID,
                        ListContract.NewsItems.COL_HEADLINE,
                        ListContract.NewsItems.COL_AUTHOR
                },
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        lvAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        lvAdapter.swapCursor(null);
    }

    @Override
    public void onRefresh() {
        checkInternet();
    }
}
