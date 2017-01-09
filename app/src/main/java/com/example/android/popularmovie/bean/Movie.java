package com.example.android.popularmovie.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/1/6.
 */

public class Movie implements Parcelable {
    private String title;//片名
    private String poster;//ctrl+shift+u大小写转换
    private String overview;//概观
    private double vote_average;//平均投票
    private String release_date;//上映日期
    private double popularity;//最受欢迎

    public Movie(String title, String poster, String overview, double vote_average, String release_date, double popularity) {
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public double getPopularity() {
        return popularity;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        poster = in.readString();
        overview = in.readString();
        vote_average = in.readDouble();
        release_date = in.readString();
        popularity = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeDouble(vote_average);
        dest.writeString(release_date);
        dest.writeDouble(popularity);
    }
}
