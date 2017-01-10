package com.example.android.popularmovie.asyncTask;

/**
 * Created by Administrator on 2017/1/10.
 */

public interface AsyncTaskCompleteListener <T>{
    public void onTaskComplete(T result);
}
