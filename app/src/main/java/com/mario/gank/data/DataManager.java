package com.mario.gank.data;


import com.mario.gank.bean.BaseGankData;
import com.mario.gank.bean.GankDaily;
import com.mario.gank.model.impl.DailyModel;
import com.mario.gank.model.impl.DataModel;
import com.mario.gank.ui.presenter.MainPresenter;
import com.mario.gank.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Mario on 2016/7/8.
 */
public class DataManager {

    private static DataManager dataManager;

    private DataModel dataModel;
    private DailyModel dailyModel;

    public synchronized static DataManager getInstance(){
        if(dataManager == null){
            dataManager = new DataManager();
        }
        return dataManager;
    }

    private DataManager(){
        this.dataModel = DataModel.getInstance();
        this.dailyModel = DailyModel.getInstance();
    }

    public Observable<List<GankDaily>> getDailyDataByNetwork(MainPresenter.EasyDate currentDate){
        return Observable.just(currentDate)
                .flatMapIterable(MainPresenter.EasyDate::getPastTime)
                .flatMap(easyDate -> {

                    return this.dailyModel.getDaily(easyDate.getYear(),
                            easyDate.getMonth(), easyDate.getDay())
                            .filter(dailyData ->
                                    dailyData.results.androidData != null);
                })
                .toSortedList((dailyData, dailyData2) -> {
                    return dailyData2.results.androidData.get(0).publishedAt.compareTo(
                            dailyData.results.androidData.get(0).publishedAt);
                })
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }

    public Observable<ArrayList<BaseGankData>> getDataByNetWork(String type, int size, int page) {
        return this.dataModel.getData(type, size, page)
                .map(gankData -> gankData.results)
                .compose(RxUtils.applyIOToMainThreadSchedulers());
    }

    public Observable<ArrayList<ArrayList<BaseGankData>>> getDailyDetailByDailyResults(GankDaily.DailyResults results) {
        return Observable.just(results).map(dailyResults -> {
            ArrayList<ArrayList<BaseGankData>> cardData = new ArrayList<>();
            if (dailyResults.welfareData != null && dailyResults.welfareData.size() > 0) {
                cardData.add(dailyResults.welfareData);
            }
            if (dailyResults.androidData != null && dailyResults.androidData.size() > 0) {
                cardData.add(dailyResults.androidData);
            }
            if (dailyResults.iosData != null && dailyResults.iosData.size() > 0) {
                cardData.add(dailyResults.iosData);
            }
            if (dailyResults.jsData != null && dailyResults.jsData.size() > 0) {
                cardData.add(dailyResults.jsData);
            }
            if (dailyResults.videoData != null && dailyResults.videoData.size() > 0) {
                cardData.add(dailyResults.videoData);
            }
            if (dailyResults.resourcesData != null && dailyResults.resourcesData.size() > 0) {
                cardData.add(dailyResults.resourcesData);
            }
            if (dailyResults.appData != null && dailyResults.appData.size() > 0) {
                cardData.add(dailyResults.appData);
            }
            if (dailyResults.recommendData != null && dailyResults.recommendData.size() > 0) {
                cardData.add(dailyResults.recommendData);
            }
            return cardData;
        }).compose(RxUtils.applyIOToMainThreadSchedulers());
    }
}
