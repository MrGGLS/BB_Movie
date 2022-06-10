package com.ggls.myapp.entity;

import java.io.Serializable;

public class MovieEntity implements Serializable {
    private String id;
    private String name;
    private String summary;
    private String genre;
    private Double rate;
    private Integer year;
    private Integer runtime;
    private String imgUrl;

    @Override
    public String toString() {
        return "MovieEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", genre='" + genre + '\'' +
                ", rate=" + rate +
                ", year=" + year +
                ", runtime=" + runtime +
                ", imgUrl='" + imgUrl + '\'' +
                ", ttl='" + ttl + '\'' +
                '}';
    }

    private String ttl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }
}
