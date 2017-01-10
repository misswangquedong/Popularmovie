package com.example.android.popularmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.popularmovie.R;
import com.example.android.popularmovie.bean.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/9.
 */
public class MovieAdapter extends BaseAdapter {
    Context context;
    ArrayList<Movie> arrayList;
    private LayoutInflater layoutInflater;

    public MovieAdapter(Context contextx ,ArrayList<Movie> arrayList) {
        this.context = contextx;
        this.arrayList=arrayList;
        layoutInflater = LayoutInflater.from(contextx);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();

        } else {
            convertView = layoutInflater.inflate(R.layout.gridview_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500" + arrayList.get(position).getPoster_path())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.poster_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
        return convertView;
    }
    static class ViewHolder {//静态优先执行
        @BindView(R.id.gridview_item_poster)
        ImageView poster_image;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}