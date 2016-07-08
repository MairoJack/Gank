package com.mario.gank.ui.presenter.ivview;

import com.mario.gank.bean.BaseGankData;
import com.mario.gank.bean.GankDaily;
import com.mario.gank.core.mvp.MvpView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 2016/7/8.
 */
public interface MainView extends MvpView{

    /**
     * 查询 每日干货 成功
     *
     * @param dailyData dailyData
     * @param refresh 是否刷新
     */
    void onGetDailySuccess(List<GankDaily> dailyData, boolean refresh);

    /**
     * 查询 ( Android、iOS、前端、拓展资源、福利、休息视频 ) 成功
     *
     * @param data data
     * @param refresh 是否刷新
     */
    void onGetDataSuccess(List<BaseGankData> data, boolean refresh);

    /**
     * 切换数据源成功
     *
     * @param type type
     */
    void onSwitchSuccess(int type);

    /**
     * 获取每日详情数据
     *
     * @param title title
     * @param detail detail
     */
    void getDailyDetail(String title, ArrayList<ArrayList<BaseGankData>> detail);
}
