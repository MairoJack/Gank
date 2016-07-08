package com.mario.gank.model;


import com.mario.gank.bean.GankData;

import rx.Observable;

/**
 * Created by Mario on 2016/7/8.
 */
public interface IDataModel {

    /**
     * 分页查询( Android、iOS、前端、拓展资源、福利、休息视频 )数据
     *
     * @param type 数据类型
     * @param size 数据个数
     * @param page 第几页
     * @return Observable<GankData>
     */
    Observable<GankData> getData(String type, int size, int page);
}
