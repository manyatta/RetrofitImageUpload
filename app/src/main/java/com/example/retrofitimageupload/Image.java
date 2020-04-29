package com.example.retrofitimageupload;

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("title")
    private  String Title;
    @SerializedName("image")
    private String Image;

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }
}
