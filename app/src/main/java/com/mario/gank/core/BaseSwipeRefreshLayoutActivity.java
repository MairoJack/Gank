package com.mario.gank.core;

import android.os.Bundle;

import com.mario.gank.R;
import com.mario.gank.widget.MultiSwipeRefreshLayout;

import butterknife.BindView;


public abstract class BaseSwipeRefreshLayoutActivity extends BaseToolbarActivity {

    @BindView(R.id.multi_swipe_refresh_layout) protected MultiSwipeRefreshLayout
            mMultiSwipeRefreshLayout;

    private boolean refreshStatus = false;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.initMultiSwipeRefreshLayout();
    }


    /**
     * 初始化MultiSwipeRefreshLayout
     */
    private void initMultiSwipeRefreshLayout() {
        // 下拉刷新的颜色
        if (this.mMultiSwipeRefreshLayout != null) {
            this.mMultiSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        }

        // 在刷新时，关闭刷新开关
        if (this.mMultiSwipeRefreshLayout != null) {
            this.mMultiSwipeRefreshLayout.setOnRefreshListener(() -> onSwipeRefresh());
        }
    }


    /**
     * 刷新的时候
     */
    public abstract void onSwipeRefresh();


    /**
     * 设置刷新状态
     *
     * @param status status
     */
    public void setRefreshStatus(boolean status) {
        this.refreshStatus = status;
    }


    /**
     * 获取当前刷新状态
     *
     * @return boolean
     */
    public boolean isRefreshStatus() {
        return refreshStatus;
    }


    /**
     * 刷新 true false
     *
     * @param refresh refresh
     */
    public void refresh(final boolean refresh) {
        if (this.mMultiSwipeRefreshLayout == null) return;
        /*
         * refresh 只要进来是false 就不考虑 refreshStatus
         * 所以用了短路&&，则直接关掉
         */
        if (!refresh && this.refreshStatus) {
            // 到这了 refresh==false && refreshStatus==true
            this.mMultiSwipeRefreshLayout.postDelayed(() -> {
                BaseSwipeRefreshLayoutActivity.this.mMultiSwipeRefreshLayout.setRefreshing(false);
                BaseSwipeRefreshLayoutActivity.this.refreshStatus = false;
            }, 1666);
        } else if (!this.refreshStatus) {
            /*
             * 到这了，refresh==true，refreshStatus==false
             * 排除了refreshStatus==true的情况
             */
            this.mMultiSwipeRefreshLayout.post(
                    () -> BaseSwipeRefreshLayoutActivity.this.mMultiSwipeRefreshLayout.setRefreshing(
                            true));
            this.refreshStatus = true;
        }
    }
}
