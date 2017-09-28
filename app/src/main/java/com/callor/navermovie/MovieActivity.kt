package com.callor.navermovie


import android.databinding.DataBindingUtil
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.callor.navermovie.databinding.ActivityMovieBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.util.*

class MovieActivity : AppCompatActivity() {

    // 전역변수로 사용하기 위해 선언만 하고 초기화를 미룰경우
    // lateinit 키워드를 사용한다.
    lateinit var movieBinding: ActivityMovieBinding

    //    movieBinding : ActivityMovieBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie)// null // = ActivityMovieBinding()
    //    var movieBinding = ActivityMovieBinding()
//    var movies: MutableList<NaverMovieVO>? = null
    lateinit var movies : ArrayList<NaverMovieVO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie)
        movieBinding.btnMovieSearch.setOnClickListener(View.OnClickListener {
            val movieSearch = movieBinding.txtMovieSearch.text.toString()
            if (movieSearch == "") {
                Toast.makeText(this@MovieActivity, "검색어를 입력하세요", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            val naverSearchAsync = NaverSearchAsync()
            naverSearchAsync.execute()
        })
    }

    /**
     * override fun doInBackground(vararg params: Void?): String? {
    // ...
    }

    override fun onPreExecute() {
    super.onPreExecute()
    // ...
    }

    override fun onPostExecute(result: String?) {
    super.onPostExecute(result)
    // ...
    }
     */

    internal inner class NaverSearchAsync : AsyncTask<Int, Int, String>() {
        override fun doInBackground(vararg p0: Int?): String {
            naverSearch()
            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            return "OK"
        }


        override fun onPostExecute(aVoid: String?) {
            super.onPostExecute(aVoid)
            if(movies.size > 0) {
                Log.d("movies_size", movies.size.toString())
                val adapter = MovieAdapter(movies)
                movieBinding.movieListView.adapter = adapter

                val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                movieBinding.movieListView.layoutManager = layoutManager
            } else {
                Toast.makeText(this@MovieActivity,"자료가 없음",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun naverSearch() {
        val movieSearch = movieBinding.txtMovieSearch.text.toString()
        try {

            val queryString = URLEncoder.encode(movieSearch, "UTF-8")
            var apiURL = "https://openapi.naver.com/v1/search/movie.json"
            apiURL += "?query=" + queryString
            apiURL += "&display=20"

            Log.d("NAVER", apiURL)

            val url = URL(apiURL)
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.setRequestProperty("X-Naver-Client-Id", NaverSecret.NAVER_CLIENT_ID)
            connection.setRequestProperty("X-Naver-Client-Secret", NaverSecret.NAVER_CLIENT_SECRET)

            // 데이터를 보내줄래?? 물어보기
            val responseCode = connection.responseCode
            val buffer: BufferedReader
            if (responseCode == 200) { // 보내줄께.. 하면
                buffer = BufferedReader(InputStreamReader(connection.inputStream))
            } else {
                return
            }
            var jsonString = String() // String을 계속 이어받기 위해서 String 객체로 생성한다.
            while (true) {
                var inputLine: String? = buffer.readLine() //
                if (inputLine == null) {
                    break
                }
                jsonString += inputLine
            }
            buffer.close() // 네트워크 종료

            // 네이버로 부터 받은 Json 구조의 String json Object 로 변경해서 사용한다.
            val resJSON = JSONObject(jsonString)

            // json object에서 items 이하의 json tree(Array)만을 추출한다.
            val items = resJSON.getJSONArray("items")

            movies = ArrayList()
            for (i in 0..items.length() - 1 step(1) ) {

                // 하부 tree를 하나씩 추출한다.
                val item = items.getJSONObject(i)

//                var vo = NaverMovieVO()
                NaverMovieVO().let { vo ->
                    vo.title = item.getString("title")
                    vo.link = item.getString("link")
                    vo.image = item.getString("image")
                    vo.director = item.getString("director")
                    vo.actor = item.getString("actor")
                    vo.pubDate = item.getString("pubDate")
                    vo.subtitle = item.getString("subtitle")
                    vo.userRating = item.getString("userRating")

                    Log.d("NAVER", item.getString("title"))
                    movies.add(vo)
                }
            }

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}
