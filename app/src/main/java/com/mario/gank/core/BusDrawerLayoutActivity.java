package com.mario.gank.core;

import android.os.Bundle;
import rx.subscriptions.CompositeSubscription;


public abstract class BusDrawerLayoutActivity extends BaseDrawerLayoutActivity {

    public CompositeSubscription mCompositeSubscription;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCompositeSubscription = new CompositeSubscription();
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        this.mCompositeSubscription = null;
    }
}
