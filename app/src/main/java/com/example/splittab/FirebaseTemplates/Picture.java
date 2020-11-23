package com.example.splittab.FirebaseTemplates;

public class Picture {

   private String imgURL;

    public Picture(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
