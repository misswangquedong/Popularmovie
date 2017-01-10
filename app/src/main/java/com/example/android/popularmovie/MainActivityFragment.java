package com.example.android.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.android.popularmovie.adapter.MovieAdapter;
import com.example.android.popularmovie.asyncTask.AsyncTaskCompleteListener;
import com.example.android.popularmovie.asyncTask.FetchMyDataTask;
import com.example.android.popularmovie.bean.Movie;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ArrayList<Movie> arrayList;
    private ListAdapter movieAdapter;
    private GridView gridView;

    private String sortOrder;
    private SharedPreferences prefs;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);  //设置有选项菜单
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }


    @Override
    public void onStart() {
        super.onStart();
        String order = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popularity));

        if (order != sortOrder || arrayList == null) {
            sortOrder = order;
            if (isOnline()) {
                new FetchMyDataTask(
                        new FetchMyDataTaskCompleteListener()).execute(sortOrder);
            } else {
                Toast.makeText(getActivity(), "网络连接没有打开", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView_movie);
        gridView.setNumColumns(2);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Movie movieItem = (Movie) movieAdapter.getItem(position);
                intent.putExtra("MOVIEOBJECT", movieItem);
                startActivity(intent);

            }
        });
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    /*
        判断是否有网络
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.fragment_main_refresh) {
            if (isOnline()) {
                new FetchMyDataTask(
                        new FetchMyDataTaskCompleteListener()).execute(sortOrder);
                return true;
            } else {
                Toast.makeText(getActivity(), "网络连接没有打开", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchMyDataTaskCompleteListener implements AsyncTaskCompleteListener<ArrayList<Movie>> {
        @Override
        public void onTaskComplete(ArrayList<Movie> result) {
            movieAdapter = new MovieAdapter(getActivity(), result);
            gridView.setAdapter(movieAdapter);
        }

    }
}
