package com.callor.navermovie;

import android.os.Build;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.callor.navermovie.databinding.MovieItemViewBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by callor on 2017-09-14.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<NaverMovieVO> movies ;
    public MovieAdapter(List<NaverMovieVO> movies) {
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MovieItemViewBinding itemViewBinding = MovieItemViewBinding.inflate(layoutInflater,parent,false);
        return new MovieHolder(itemViewBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieHolder mHolder = (MovieHolder)holder ;

        String mTitle = movies.get(position).getTitle();
        String mSubTitle = "<font color=blue>"+ movies.get(position).getSubtitle() + "</font>";
        mHolder.itemViewBinding.movieItemTitle.setText(this.getHTML(mTitle+"("+mSubTitle +")"));

        String mImageLink = movies.get(position).getImage();
        Picasso.with(mHolder.itemView.getContext())
                .load(mImageLink)
                .into(mHolder.itemViewBinding.movieItemImage);

        String mDirector = "<b>감독</b>:"+movies.get(position).getDirector();
        mHolder.itemViewBinding.movieItemDirector.setText(this.getHTML(mDirector));

        String mActor = "출연:" + movies.get(position).getActor();
        mHolder.itemViewBinding.movieItemActor.setText(this.getHTML(mActor));

        String mPubDate = "제작연도:" + movies.get(position).getPubDate();
        mHolder.itemViewBinding.movieItemPubDate.setText(mPubDate);

        String mUserRating = movies.get(position).getUserRating();

        // 평점 만큼 별찍기
        float temp  =Float.valueOf(mUserRating);
        int userRating = (int)temp;

        String star = new String();
        for(int i = 0 ; i < userRating;i++) {
            star += "★";
        }
        mHolder.itemViewBinding.movieItemUserRating
                .setText(this.getHTML(mUserRating+"(<font color=blue>" + star + "</font>)"));

    }
    // String 받아서 TextView에 뿌릴 HTML 코드 형식으로 변환하는 사용자 정의 Method
    private Spanned getHTML(String strText) {
        Spanned spText = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spText = Html.fromHtml(strText,Html.FROM_HTML_MODE_LEGACY);
        } else {
            spText = Html.fromHtml(strText);
        }
        return spText ;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {
        private final MovieItemViewBinding itemViewBinding ;
        public MovieHolder(MovieItemViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding ;
        }
    }
}
