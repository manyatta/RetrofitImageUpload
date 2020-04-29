package com.example.retrofitimageupload;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static  final String BASE_URL = "https://interviewmovetech.000webhostapp.com/";
    //private static  final String BASE_URL = "http://192.168.43.140/000Webhost/";
    private static Retrofit retrofit;

    public  static  Retrofit getApiClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
