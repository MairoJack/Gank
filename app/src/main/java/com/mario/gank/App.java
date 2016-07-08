package com.mario.gank;

import android.app.Application;
import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mario.gank.api.ApiConstant;
import com.orhanobut.logger.Logger;


/**
 * Created by Mario on 2016/7/8.
 */
public class App extends Application {

    private static App mInstance = new App();
    public boolean log = true;
    public Gson gson;

    public static final long ONE_KB = 1024L;
    public static final long ONE_MB = ONE_KB * 1024L;
    public static final long CACHE_DATA_MAX_SIZE = ONE_MB * 3L;

    public static App getInstance(){
        return mInstance;
    }

    @Override public void onCreate() {
        super.onCreate();
        mInstance = this;
        Logger.init();
        this.initGson();
        this.initReservoir();
    }


    private void initGson() {
        this.gson = new GsonBuilder().setDateFormat(ApiConstant.GANK_DATA_FORMAT).create();
    }


    private void initReservoir() {
        try {
            Reservoir.init(this, CACHE_DATA_MAX_SIZE, this.gson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
