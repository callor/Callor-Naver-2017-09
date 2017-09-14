package com.callor.navermovie;

import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.callor.navermovie.databinding.ActivityMovieBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {

    ActivityMovieBinding movieBinding ;
    List<NaverMovieVO> movies ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieBinding = DataBindingUtil.setContentView(this,R.layout.activity_movie);

        movieBinding.btnMovieSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String movieSearch = movieBinding.txtMovieSearch.getText().toString();
                if(movieSearch.isEmpty()) {
                    Toast.makeText(MovieActivity.this,"검색어를 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                NaverSearchAsync naverSearchAsync = new NaverSearchAsync();
                naverSearchAsync.execute();

            }
        });
    }

    class NaverSearchAsync extends AsyncTask<Integer,Integer,Void>{

        // Async 모드를 이용해서 naverSearch() method 호출
        @Override
        protected Void doInBackground(Integer... integers) {
            naverSearch();
            return null;
        }
    }

    public void naverSearch(){
        String movieSearch = movieBinding.txtMovieSearch.getText().toString();
        try {

            String queryString = URLEncoder.encode(movieSearch,"UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/movie.json";
            apiURL += "?query=" + queryString ;
            apiURL += "&display=20";

            Log.d("NAVER",apiURL);

            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Naver-Client-Id",NaverSecret.NAVER_CLIENT_ID);
            connection.setRequestProperty("X-Naver-Client-Secret",NaverSecret.NAVER_CLIENT_SECRET);

            // 데이터를 보내줄래?? 물어보기
            int responseCode = connection.getResponseCode();
            BufferedReader buffer ;
            if(responseCode == 200) { // 보내줄께.. 하면
                buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                return;
            }
            String inputLine;
            String jsonString = new String(); // String을 계속 이어받기 위해서 String 객체로 생성한다.
            while( (inputLine = buffer.readLine()) != null ) {
                jsonString += inputLine;
            }
            buffer.close(); // 네트워크 종료

            // 네이버로 부터 받은 Json 구조의 String json Object 로 변경해서 사용한다.
            JSONObject resJSON = new JSONObject(jsonString);

            // json object에서 items 이하의 json tree(Array)만을 추출한다.
            JSONArray items = resJSON.getJSONArray("items");

            movies = new ArrayList<NaverMovieVO>();
            for(int i = 0 ; i < items.length() ; i++) {
                // 하부 tree를 하나씩 추출한다.
                JSONObject item = items.getJSONObject(i);

                NaverMovieVO vo = new NaverMovieVO();
                vo.setTitle(item.getString("title"));
                vo.setLink(item.getString("link"));
                vo.setImage(item.getString("image"));
                vo.setDirector(item.getString("director"));
                vo.setActor(item.getString("actor"));
                vo.setPubDate(item.getString("pubDate"));
                vo.setSubtitle(item.getString("subtitle"));
                vo.setUserRating(item.getString("userRating"));

                Log.d("NAVER",item.getString("title"));
                movies.add(vo);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
