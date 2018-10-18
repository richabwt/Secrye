package com.example.bosswebtech.secrye.Network.Retrofit;

import com.example.bosswebtech.secrye.Utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Brajendr on 8/8/2016.
 */
public class RFClient {

    public static final String BASE_URL = Constants.BASE_URL;


    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static RFInterface getApiService() {
        return getRetrofitInstance().create(RFInterface.class);
    }


}
