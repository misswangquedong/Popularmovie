package com.example.android.popularmovie.asyncTask;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovie.BuildConfig;
import com.example.android.popularmovie.bean.Movie;
import com.example.android.popularmovie.bean.MyMovie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 */

public class FetchMyDataTask extends AsyncTask<String, Void, ArrayList<Movie>> {
    private final String LOG_TAG = FetchMyDataTask.class.getSimpleName();

    private ArrayList<Movie> arrayList;
    private Context context;
    private AsyncTaskCompleteListener<ArrayList<Movie>> listener;
    private String movieJsonStr;

    public FetchMyDataTask(AsyncTaskCompleteListener<ArrayList<Movie>> listener) {
        //this.context = ctx;
        this.listener = listener;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;//缓冲读者

        try {
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";

            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter("language", "zh")
                    .appendQueryParameter(APPID_PARAM, BuildConfig.Movie_API_KEY)
                    .build();
            URL url = new URL(builtUri.toString());
            System.out.println(url);
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
            //解析JSON在此调用
            arrayList = getMovieDataFromJson(movieJsonStr);

            return arrayList;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
    /*
    解析json
     */
    private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {
        Gson gson = new Gson();
        Type type = new TypeToken<MyMovie<List<Movie>>>(){}.getType();

        MyMovie myMovie = gson.fromJson(movieJsonStr,type);
        System.out.println(myMovie);

        arrayList = (ArrayList<Movie>) myMovie.getResults();
        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> arrayList) {
        listener.onTaskComplete(arrayList);
    }

}
