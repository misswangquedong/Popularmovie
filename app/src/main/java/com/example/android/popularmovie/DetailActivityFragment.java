package com.example.android.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovie.bean.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.detail_fragment_title)
    TextView title;
    @BindView(R.id.detail_fragment_poster)
    ImageView poster_image;
    @BindView(R.id.detail_fragment_overview)
    TextView overview;
    @BindView(R.id.detail_fragment_release_date)
    TextView release_date;
    @BindView(R.id.detail_fragment_vote_average)
    TextView vote_average;
    private Movie m;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);  //设置有选项菜单
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_detail, container, false);
        unbinder=ButterKnife.bind(this, rootView);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("MOVIEOBJECT")) {
            m = (Movie) intent.getParcelableExtra("MOVIEOBJECT");
            if (m != null) {
                initView(m);
            }
        }
        return rootView;
    }

    /*
    初始化view
     */
    private void initView(Movie m) {
        title.setText(m.getTitle());
        overview.setText(m.getOverview());
        release_date.setText(m.getRelease_date());
        vote_average.setText(m.getVote_average() + " 分");
        Picasso.with(getActivity())
                .load("https://image.tmdb.org/t/p/w500" + m.getPoster())
                .placeholder(R.mipmap.ic_launcher)
                .into(poster_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
