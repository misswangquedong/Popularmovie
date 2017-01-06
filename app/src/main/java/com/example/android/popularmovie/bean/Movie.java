package com.example.android.popularmovie.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/6.
 */

public class Movie  implements Serializable {
    private String title;//片名
    private String poster;//ctrl+shift+u大小写转换
    private String overview;//概观
    private Double vote_average;//平均投票
    private String release_date;//上映日期
    private Double popularity;//最受欢迎

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", poster='" + poster + '\'' +
                ", overview='" + overview + '\'' +
                ", vote_average=" + vote_average +
                ", release_date='" + release_date + '\'' +
                ", popularity=" + popularity +
                '}';
    }
    /* @Override
    public int compareTo(Movie movie) {
        if(this.popularity>movie.popularity){
            return 1 ;
        }else if(this.popularity<movie.popularity){
            return -1 ;
        }else{
            return this.popularity.compareTo(movie.popularity) ;	// 调用String中的compareTo()方法
        }
    }*/
}
