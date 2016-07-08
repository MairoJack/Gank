package com.mario.gank.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.camnter.easyrecyclerview.widget.EasyRecyclerView;
import com.camnter.easyrecyclerview.widget.decorator.EasyBorderDividerItemDecoration;
import com.mario.gank.R;
import com.mario.gank.bean.BaseGankData;
import com.mario.gank.core.BaseToolbarActivity;
import com.mario.gank.ui.adapter.DailyDetailAdapter;
import com.mario.gank.utils.IntentUtils;

import java.util.ArrayList;

import butterknife.BindView;


public class DailyDetailActivity extends BaseToolbarActivity
        implements DailyDetailAdapter.onCardItemClickListener {

    @BindView(R.id.daily_detail_rv) EasyRecyclerView dailydailyDetailRv;
    private DailyDetailAdapter detailAdapter;

    private static final String EXTRA_DETAIL = "com.camnter.easygank.EXTRA_DETAIL";
    private static final String EXTRA_TITLE = "com.camnter.easygank.EXTRA_TITLE";


    public static void startActivity(Context context, String title, ArrayList<ArrayList<BaseGankData>> detail) {
        Intent intent = new Intent(context, DailyDetailActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DETAIL, detail);
        context.startActivity(intent);
    }


    /**
     * Fill in layout id
     *
     * @return layout id
     */
    @Override protected int getLayoutId() {
        return R.layout.activity_daily_detail;
    }


    /**
     * Initialize the view in the layout
     *
     * @param savedInstanceState savedInstanceState
     */
    @SuppressLint("InflateParams") @Override protected void initViews(Bundle savedInstanceState) {
        EasyBorderDividerItemDecoration detailDecoration = new EasyBorderDividerItemDecoration(
                this.getResources().getDimensionPixelOffset(R.dimen.data_border_divider_height),
                this.getResources()
                    .getDimensionPixelOffset(R.dimen.data_border_padding_infra_spans));
        this.dailydailyDetailRv.addItemDecoration(detailDecoration);
        this.detailAdapter = new DailyDetailAdapter(this);
        this.detailAdapter.setOnCardItemClickListener(this);
        this.dailydailyDetailRv.setAdapter(this.detailAdapter);
        this.showBack();
        this.setTitle(this.getDetailTitle());
    }


    /**
     * Initialize the View of the listener
     */
    @Override protected void initListeners() {

    }


    /**
     * Initialize the Activity data
     */
    @Override protected void initData() {
        this.detailAdapter.setList(this.getDetail());
        this.detailAdapter.notifyDataSetChanged();
    }


    @SuppressWarnings("unchecked") private ArrayList<ArrayList<BaseGankData>> getDetail() {
        return (ArrayList<ArrayList<BaseGankData>>) IntentUtils.getSerializableExtra(
                this.getIntent(), EXTRA_DETAIL);
    }


    private String getDetailTitle() {
        return IntentUtils.getStringExtra(this.getIntent(), EXTRA_TITLE);
    }


    @Override public void onCardItemOnClick(String urlType, String title, String url) {
        EasyWebViewActivity.toUrl(this, url, title, urlType);
    }


    @Override public void onWelfareOnClick(String url, String title, View v) {
        PictureActivity.startActivityByActivityOptionsCompat(this, url, title, v);
    }


    /*********
     * Umeng *
     *********/

    public void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }


    public void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    /*********
     * Umeng *
     *********/

}
