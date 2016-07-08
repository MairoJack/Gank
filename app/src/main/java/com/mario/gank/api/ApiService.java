package com.mario.gank.api;

import com.mario.gank.bean.GankDaily;
import com.mario.gank.bean.GankData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Mario on 2016/7/8.
 */
public interface ApiService {

    /**
     * @param year  year
     * @param month month
     * @param day   day
     * @return Observable<GankDaily>
     */
    @GET("day/{year}/{month}/{day}")
    Observable<GankDaily> getDaily(
            @Path("year") int year, @Path("month") int month, @Path("day") int day);

    @GET("data/{type}/{size}/{page}")
    Observable<GankData> getData(
            @Path("type") String type, @Path("size") int size, @Path("page") int page);

}
