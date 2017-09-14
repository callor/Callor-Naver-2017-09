package com.callor.navermovie;

/**
 * Created by callor on 2017-09-14.
 */
public class NaverMovieVO {
    private String title ; // 영화 제목
    private String link ; // 네이버 상세보기 링크
    private String image ; // 영화 포스터
    private String subtitle ; // 영문문제목
    private String pubDate ; // 제작연도
    private String director ; // 감독
    private String actor ; // 출연배우
    private String userRating ; // 유저 평점

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }
}
