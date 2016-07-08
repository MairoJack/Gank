package com.mario.gank.ui.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mario.gank.App;
import com.mario.gank.R;
import com.mario.gank.core.BaseToolbarActivity;
import com.mario.gank.ui.presenter.PicturePresenter;
import com.mario.gank.ui.presenter.ivview.PictureView;
import com.mario.gank.utils.DeviceUtils;
import com.mario.gank.utils.IntentUtils;
import com.mario.gank.utils.ShareUtils;
import com.mario.gank.utils.ToastUtils;

import butterknife.BindView;


public class PictureActivity extends BaseToolbarActivity
        implements PictureView, View.OnLongClickListener {

    private static final String EXTRA_URL = "com.camnter.easygank.EXTRA_URL";
    private static final String EXTRA_TITLE = "com.camnter.easygank.EXTRA_TITLE";

    private static final String SHARED_ELEMENT_NAME = "PictureActivity";

    @BindView(R.id.picture_iv) ImageView pictureIV;

    private PicturePresenter presenter;

    private GlideBitmapDrawable glideBitmapDrawable;


    public static void startActivity(Context context, String url, String title) {
        context.startActivity(createIntent(context, url, title));
    }


    public static void startActivityByActivityOptionsCompat(Activity activity, String url, String title, View view) {
        Intent intent = createIntent(activity, url, title);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                view, view.getWidth() / 2, view.getHeight() / 2, view.getWidth(), view.getHeight());
        try {
            ActivityCompat.startActivity(activity, intent, activityOptionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            startActivity(activity, url, title);
        }
    }


    private static Intent createIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }


    /**
     * Fill in layout id
     *
     * @return layout id
     */
    @Override protected int getLayoutId() {
        return R.layout.activity_picture;
    }


    /**
     * Initialize the view in the layout
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override protected void initViews(Bundle savedInstanceState) {
        this.pictureIV = this.findView(R.id.picture_iv);
        ViewCompat.setTransitionName(this.pictureIV, SHARED_ELEMENT_NAME);
    }


    /**
     * Initialize the View of the listener
     */
    @Override protected void initListeners() {
        this.pictureIV.setOnLongClickListener(this);
    }


    /**
     * Initialize the Activity data
     */
    @Override protected void initData() {
        this.showBack();
        this.setTitle(this.getUrlTitle());
        Glide.with(this)
             .load(this.getUrl())
             .listener(new RequestListener<String, GlideDrawable>() {
                 @Override
                 public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                     return false;
                 }


                 @Override
                 public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                     // 加载完成了
                     PictureActivity.this.glideBitmapDrawable = (GlideBitmapDrawable) resource;
                     return false;
                 }
             })
             .diskCacheStrategy(DiskCacheStrategy.ALL)
             .crossFade()
             .into(this.pictureIV)
             .getSize((width, height) -> {
                 if (!PictureActivity.this.pictureIV.isShown()) {
                     PictureActivity.this.pictureIV.setVisibility(View.VISIBLE);
                 }
             });

        this.presenter = new PicturePresenter();
        this.presenter.attachView(this);
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_picture_download:
                this.download();
                return true;
            case R.id.menu_picture_copy:
                DeviceUtils.copy2Clipboard(this, this.getUrl());
                Snackbar.make(this.pictureIV, this.getString(R.string.common_copy_success),
                        Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.menu_picture_share:
                this.download();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private String getUrl() {
        return IntentUtils.getStringExtra(this.getIntent(), EXTRA_URL);
    }


    private String getUrlTitle() {
        return IntentUtils.getStringExtra(this.getIntent(), EXTRA_TITLE);
    }


    @Override protected void onDestroy() {
        if (this.glideBitmapDrawable != null) {
            this.glideBitmapDrawable.setCallback(null);
            this.glideBitmapDrawable = null;
        }
        this.presenter.detachView();
        super.onDestroy();
    }


    /**
     * 下载成功
     *
     * @param path path
     */
    @Override public void onDownloadSuccess(String path) {
        ToastUtils.show(this, path, Toast.LENGTH_SHORT);
    }


    /**
     * 分享
     *
     * @param uri uri
     */
    @Override public void onShare(Uri uri) {
        ShareUtils.shareImage(this, uri, "分享妹子");
    }


    /**
     * 发生错误
     *
     * @param e e
     */
    @Override public void onFailure(Throwable e) {
        Snackbar.make(this.pictureIV, this.getString(R.string.common_network_error),
                Snackbar.LENGTH_LONG).show();
    }


    public void download() {
        if (this.glideBitmapDrawable != null) {
            this.presenter.downloadPicture(this.glideBitmapDrawable, this,
                    App.getInstance());
        } else {
            Snackbar.make(this.pictureIV, this.getString(R.string.picture_loading),
                    Snackbar.LENGTH_LONG).show();
        }
    }


    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     * @return true if the callback consumed the long click, false otherwise.
     */
    @Override public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.picture_iv:
                new AlertDialog.Builder(PictureActivity.this).setMessage(
                        getString(R.string.picture_download))
                                                             .setNegativeButton(
                                                                     android.R.string.cancel,
                                                                     (dialog, which) -> dialog.dismiss())
                                                             .setPositiveButton(android.R.string.ok,
                                                                     (dialog, which) -> {
                                                                         this.download();
                                                                         dialog.dismiss();
                                                                     })
                                                             .show();
                return true;
        }
        return false;
    }


    /*********
     * Umeng *
     *********/

    public void onResume() {
        super.onResume();
       // MobclickAgent.onResume(this);
    }


    public void onPause() {
        super.onPause();
       // MobclickAgent.onPause(this);
    }

    /*********
     * Umeng *
     *********/

}
