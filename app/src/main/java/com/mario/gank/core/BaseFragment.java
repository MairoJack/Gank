package com.mario.gank.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mario.gank.utils.ToastUtils;

/**
 * Created by Mario on 2016/7/8.
 */
public abstract class BaseFragment extends Fragment{

    protected View self;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if(this.self == null){
            this.self = inflater.inflate(this.getLayoutId(),container,false);
        }
        if(this.self.getParent() != null){
            ViewGroup parent = (ViewGroup) this.self.getParent();
            parent.removeView(this.self);
        }
        this.initViews(this.self, savedInstanceState);
        this.initData();
        this.initListeners();
        return this.self;
    }

    protected abstract int getLayoutId();

    protected abstract void initViews(View self, Bundle savedInstanceState);

    protected abstract void initListeners();

    protected abstract void initData();

    protected <V extends View> V findView(int id) {
        return (V) this.self.findViewById(id);
    }


    public void showToast(String msg) {
        this.showToast(msg, Toast.LENGTH_SHORT);
    }


    public void showToast(String msg, int duration) {
        if (msg == null) return;
        if (duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG) {
            ToastUtils.show(this.getActivity(), msg, duration);
        } else {
            ToastUtils.show(this.getActivity(), msg, ToastUtils.LENGTH_SHORT);
        }
    }


    public void showToast(int resId) {
        this.showToast(resId, Toast.LENGTH_SHORT);
    }


    public void showToast(int resId, int duration) {
        if (duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG) {
            ToastUtils.show(this.getActivity(), resId, duration);
        } else {
            ToastUtils.show(this.getActivity(), resId, ToastUtils.LENGTH_SHORT);
        }
    }


}

