package com.example.android.bakingrecipe.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit=null;
    private static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    public static BakingService getService(){

        if(retrofit==null){


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(BakingService.class);
    }
}

