package com.example.android.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.android.popularmovie.bean.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    String movieJsonStr = null;
    private ListAdapter movieAdapter;
    private GridView gridView;
    private ArrayList<Movie> arraylist;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isOnline()) {
            updateMovie();
        } else {
            Toast.makeText(getActivity(), "网络连接没有打开", Toast.LENGTH_LONG).show();
        }
        setHasOptionsMenu(true);  //设置有选项菜单
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
                //System.out.println(movieItem);
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
                updateMovie();
            } else {
                Toast.makeText(getActivity(), "网络连接没有打开", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }


    private void updateMovie() {
        MovieTask movieTask = new MovieTask();
        movieTask.execute();
    }

    public class MovieTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final String LOG_TAG = MovieTask.class.getSimpleName();


        private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            final String MOVIES_LIST = "results";//集合
            //解析JSON，见大括号JSONObject，见小括号JSONArray
            JSONObject forecastJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = forecastJson.getJSONArray(MOVIES_LIST);
            arraylist = new ArrayList();
            for (int i = 0; i < movieArray.length(); i++) {
                Movie m = new Movie();
                m.setTitle(movieArray.getJSONObject(i).getString("title"));
                m.setPoster(movieArray.getJSONObject(i).getString("poster_path"));
                m.setOverview(movieArray.getJSONObject(i).getString("overview"));
                m.setVote_average(movieArray.getJSONObject(i).getDouble("vote_average"));
                m.setRelease_date(movieArray.getJSONObject(i).getString("release_date"));
                m.setPopularity(movieArray.getJSONObject(i).getDouble("popularity"));
                arraylist.add(m);
            }
            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            final String sort = sharedPrefs.getString(
                    getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_popularity));//第一次默认值
            System.out.println(sort);
            if (arraylist != null) {
                Collections.sort(arraylist, new Comparator<Movie>() {
                    @Override
                    public int compare(Movie o1, Movie o2) {
                        if (sort.equals(getString(R.string.pref_sort_popularity))) {
                            if (o1.getPopularity() > o2.getPopularity()) {
                                return -1;
                            } else if (o1.getPopularity() < o2.getPopularity()) {
                                return 1;
                            }
                        } else if (sort.equals(getString(R.string.pref_sort_vote_average))) {
                            if (o1.getVote_average() > o2.getVote_average()) {
                                return -1;
                            } else if (o1.getVote_average() < o2.getVote_average()) {
                                return 1;
                            }
                        }
                        return 0;
                    }
                });
            }
            return arraylist;


        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;//缓冲读者

            try {
                final String MOVIE_BASE_URL =
                        "http://api.themoviedb.org/3/movie/top_rated?language=zh";

                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.Movie_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                //  Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();//字符串缓冲区
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                //InputStreamReader输入流的读者
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");//\n换行符
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
                // Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {//无论成功或者失败都有执行的代码
                if (urlConnection != null) {
                    urlConnection.disconnect();//断开连接
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                //解析JSON在此调研
                arraylist = getMovieDataFromJson(movieJsonStr);

                return arraylist;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<Movie> arrayList) {
            movieAdapter = new MovieAdapter(getActivity());
            gridView.setAdapter(movieAdapter);
        }
    }


    private class MovieAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater layoutInflater;

        public MovieAdapter(Context contextx) {
            this.context = contextx;

            layoutInflater = LayoutInflater.from(contextx);
        }

        @Override
        public int getCount() {
            return arraylist.size();
        }

        @Override
        public Object getItem(int position) {
            return arraylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.gridview_item, parent, false);
            }
            ImageView poster_image = (ImageView) convertView.findViewById(R.id.gridview_item_poster);
            Picasso.with(context)
                    .load("https://image.tmdb.org/t/p/w500" + arraylist.get(position).getPoster())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(poster_image, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
            return convertView;
        }
    }


}


