package com.mario.gank.api;

import com.mario.gank.App;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mario on 2016/7/8.
 */
public class RetrofitManager {

    private static RetrofitManager mInstance;

    private ApiService apiService;

    public static RetrofitManager getInstance(){
        if(mInstance == null) mInstance = new RetrofitManager();
        return mInstance;
    }

    private RetrofitManager(){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(5, TimeUnit.SECONDS);
        client.readTimeout(5,TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiConstant.BASE_URL)
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(
                        App.getInstance().gson))
                .client(client.build())
                .build();

        this.apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService(){
        return apiService;
    }
}
