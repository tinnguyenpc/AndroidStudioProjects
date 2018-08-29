package com.example.hieul.hismart;

/**
 * Created by hieul on 25/10/17.
 */
public class Album {

    private String id;
    private String name;
    private String gia;
    private String thumbnail;
    private String url;


    public Album() {
    }

    public Album( String id, String name, String gias, String thumbnail, String url) {


        this.id = id;
        this.name = name;
        this.gia = gias;
        this.thumbnail = thumbnail;
        this.url = url;

    }

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

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
