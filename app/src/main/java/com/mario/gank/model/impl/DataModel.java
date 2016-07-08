package com.mario.gank.model.impl;


import com.mario.gank.api.RetrofitManager;
import com.mario.gank.bean.GankData;
import com.mario.gank.model.IDataModel;

import rx.Observable;

/**
 * Created by Mario on 2016/7/8.
 */
public class DataModel implements IDataModel {

    private static final DataModel mInstance = new DataModel();

    public static DataModel getInstance(){
        return mInstance;
    }

    private DataModel(){

    }

    @Override
    public Observable<GankData> getData(String type, int size, int page) {
        return RetrofitManager.getInstance().getApiService().getData(type, size, page);
    }
}
