package com.mario.gank.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mario on 2016/7/8.
 */
public class GankData extends Error implements Serializable {

    @SerializedName("results") public ArrayList<BaseGankData> results;
}
