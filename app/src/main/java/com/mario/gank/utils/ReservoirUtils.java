package com.mario.gank.utils;

import android.util.Log;
import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirDeleteCallback;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import java.lang.reflect.Type;
import rx.Observable;


public class ReservoirUtils {

    private static final String TAG = "ReservoirUtils";

    private static ReservoirUtils instance;


    public synchronized static ReservoirUtils getInstance() {
        if (instance == null) {
            instance = new ReservoirUtils();
        }
        return instance;
    }


    public void put(final String key, final Object object) {
        if (object == null) return;
        Reservoir.putAsync(key, object, new ReservoirPutCallback() {
            @Override public void onSuccess() {
                Log.i(TAG, "Put success: key=" + key + " object=" + object.getClass());
            }


            @Override public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }


    public boolean contains(String key) {
        try {
            return Reservoir.contains(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void delete(String key) {
        if (this.contains(key)) Reservoir.deleteAsync(key);
    }


    public void refresh(final String key, final Object object) {
        if (this.contains(key)) {
            Reservoir.deleteAsync(key, new ReservoirDeleteCallback() {
                @Override public void onSuccess() {
                    ReservoirUtils.this.put(key, object);
                }


                @Override public void onFailure(Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            ReservoirUtils.this.put(key, object);
        }
    }


    public <T> Observable<T> get(String key, Class<T> clazz) {
        return Reservoir.getAsync(key, clazz);
    }


    public <T> Observable<T> get(Class<T> clazz) {
        String key = clazz.getSimpleName();
        return get(key, clazz);
    }


    public <T> void get(final String key, final Type typeOfT, final ReservoirGetCallback<T> callback) {
        Reservoir.getAsync(key, typeOfT, callback);
    }
}
