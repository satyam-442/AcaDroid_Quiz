package com.example.acadroidquiz.Modal;

public class CategoryM {
    //String imageUrl, title;
    public String name, url;
    public int sets;

    public CategoryM() {
    }

    /*public CategoryM(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }*/

    public CategoryM(String name, String url, int sets) {
        this.name = name;
        this.url = url;
        this.sets = sets;
    }

    public String getNamee() {
        return name;
    }

    public void setNamee(String name) {
        this.name = name;
    }

    public String getUrll() {
        return url;
    }

    public void setUrll(String url) {
        this.url = url;
    }

    public int getSetss() {
        return sets;
    }

    public void setSetss(int sets) {
        this.sets = sets;
    }
}
