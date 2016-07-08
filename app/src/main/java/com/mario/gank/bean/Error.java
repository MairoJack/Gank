package com.mario.gank.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mario on 2016/7/8.
 */
public class Error {

    // 每个请求都有error数据
    @SerializedName("error") public Boolean error;

    @SerializedName("msg") public String msg;
}
