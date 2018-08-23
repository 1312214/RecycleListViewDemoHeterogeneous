package com.duyhoang.recyclelistviewdemoheterogeneous;

/**
 * Created by rogerh on 7/28/2018.
 */

public class Advertisement {
    private String message;
    private String urlImage;

    public Advertisement(String message){
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
