package com.ggls.myapp.entity;

import java.io.Serializable;

public class ReviewEntity implements Serializable {
    private String username;
    private String movieid;
    private Double rating;

    @Override
    public String toString() {
        return "ReviewEntity{" +
                "username='" + username + '\'' +
                ", movieid='" + movieid + '\'' +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                '}';
    }

    private String review;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
