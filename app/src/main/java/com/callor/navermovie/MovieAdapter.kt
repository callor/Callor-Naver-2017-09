package com.callor.navermovie

import android.os.Build
import android.support.annotation.IntegerRes
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

import com.callor.navermovie.databinding.MovieItemViewBinding
import com.squareup.picasso.Picasso

/**
 * Created by callor on 2017-09-14.
 */

class MovieAdapter(private var movies: List<NaverMovieVO>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemViewBinding = MovieItemViewBinding.inflate(layoutInflater, parent, false)
        return MovieHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = holder as MovieHolder

        val mTitle = movies[position].title
        val mSubTitle = "<font color=blue>" + movies[position].subtitle + "</font>"
        mHolder.itemViewBinding.movieItemTitle.text = this.getHTML("$mTitle($mSubTitle)")

        val mImageLink = movies[position].image
        try {
            Picasso.with(mHolder.itemView.context)
                    .load(mImageLink)
                    .into(mHolder.itemViewBinding.movieItemImage)
        } catch (e: Exception) {
            Log.d("Picaso", e.toString())
        }

        val mDirector = "<b>감독</b>:" + movies[position].director
        mHolder.itemViewBinding.movieItemDirector.text = this.getHTML(mDirector)

        val mActor = "출연:" + movies[position].actor
        mHolder.itemViewBinding.movieItemActor.text = this.getHTML(mActor)

        val mPubDate = "제작연도:" + movies[position].pubDate
        mHolder.itemViewBinding.movieItemPubDate.text = mPubDate

        val mUserRating = movies[position].userRating

        // 평점 만큼 별찍기
        val temp = java.lang.Float.valueOf(mUserRating)!!
        val userRating = temp.toInt()

        var star = String()
        for (i in 0 until userRating) {
            star += "★"
        }
        mHolder.itemViewBinding.movieItemUserRating.text = this.getHTML("$mUserRating(<font color=blue>$star</font>)")

    }

    // String 받아서 TextView에 뿌릴 HTML 코드 형식으로 변환하는 사용자 정의 Method
    private fun getHTML(strText: String): Spanned? {
        var spText: Spanned? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spText = Html.fromHtml(strText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            spText = Html.fromHtml(strText)
        }
        return spText
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieHolder(val itemViewBinding: MovieItemViewBinding) : RecyclerView.ViewHolder(itemViewBinding.root)
}
