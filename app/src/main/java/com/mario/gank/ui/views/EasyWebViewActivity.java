package com.mario.gank.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mario.gank.R;
import com.mario.gank.api.GankType;
import com.mario.gank.api.GankTypeDict;
import com.mario.gank.core.BaseToolbarActivity;
import com.mario.gank.utils.DeviceUtils;
import com.mario.gank.utils.IntentUtils;
import com.mario.gank.utils.ShareUtils;
import com.mario.gank.utils.ToastUtils;
import com.mario.gank.utils.WebViewUtils;

import butterknife.BindView;


public class EasyWebViewActivity extends BaseToolbarActivity {

    private static final String EXTRA_URL = "com.camnter.easygank.EXTRA_URL";
    private static final String EXTRA_TITLE = "com.camnter.easygank.EXTRA_TITLE";

    // For gank api
    private static final String EXTRA_GANK_TYPE = "com.camnter.easygank.EXTRA_GANK_TYPE";

    private static final int PROGRESS_RATIO = 1000;

    @BindView(R.id.webview_pb) ProgressBar webviewPb;
    @BindView(R.id.webview) WebView webview;

    private boolean goBack = false;
    private static final int RESET_GO_BACK_INTERVAL = 2666;
    private static final int MSG_WHAT_RESET_GO_BACK = 206;
    private final Handler mHandler = new Handler(msg -> {
        switch (msg.what) {
            case EasyWebViewActivity.MSG_WHAT_RESET_GO_BACK:
                EasyWebViewActivity.this.goBack = false;
                return true;
        }
        return false;
    });


    /**
     * @param context Any context
     * @param url A valid url to navigate to
     */
    public static void toUrl(Context context, String url) {
        toUrl(context, url, android.R.string.untitled);
    }


    /**
     * @param context Any context
     * @param url A valid url to navigate to
     * @param titleResId A String resource to display as the title
     */
    public static void toUrl(Context context, String url, int titleResId) {
        toUrl(context, url, context.getString(titleResId));
    }


    /**
     * @param context Any context
     * @param url A valid url to navigate to
     * @param title A title to display
     */
    public static void toUrl(Context context, String url, String title) {
        Intent intent = new Intent(context, EasyWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        context.startActivity(intent);
    }


    /**
     * For gank api
     *
     * @param context context
     * @param url url
     * @param title title
     * @param type type
     */
    public static void toUrl(Context context, String url, String title, String type) {
        Intent intent = new Intent(context, EasyWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_GANK_TYPE, type);
        context.startActivity(intent);
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.web_refresh:
                this.refreshWebView();
                return true;
            case R.id.web_copy:
                DeviceUtils.copy2Clipboard(this, this.webview.getUrl());
                Snackbar.make(this.webview, this.getString(R.string.common_copy_success),
                        Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.menu_web_share:
                ShareUtils.share(this, this.getUrl());
                return true;
            case R.id.web_switch_screen_mode:
                this.switchScreenConfiguration(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.setAppBarLayoutVisibility(true);
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.setAppBarLayoutVisibility(false);
        }
    }


    public void switchScreenConfiguration(MenuItem item) {
        if (this.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            if (item != null) item.setTitle(this.getString(R.string.menu_web_vertical));
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            if (item != null) item.setTitle(this.getString(R.string.menu_web_horizontal));
        }
    }


    protected void refreshWebView() {
        this.webview.reload();
    }


    /**
     * Fill in layout id
     *
     * @return layout id
     */
    @Override protected int getLayoutId() {
        return R.layout.activity_webview;
    }


    /**
     * Initialize the view in the layout
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override protected void initViews(Bundle savedInstanceState) {
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
        this.enableJavascript();
        this.enableCaching();
        this.enableCustomClients();
        this.enableAdjust();
        this.zoomedOut();
        this.webview.loadUrl(this.getUrl());
        this.showBack();
        this.setTitle(this.getUrlTitle());

        if (this.getGankType() == null) return;
        if (GankTypeDict.urlType2TypeDict.get(this.getGankType()) == GankType.video) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
    }


    private void enableCustomClients() {
        this.webview.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            /**
             * @param view The WebView that is initiating the callback.
             * @param url  The url of the page.
             */
            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("www.vmovier.com")) {
                    WebViewUtils.injectCSS(EasyWebViewActivity.this,
                            EasyWebViewActivity.this.webview, "vmovier.css");
                } else if (url.contains("video.weibo.com")) {
                    WebViewUtils.injectCSS(EasyWebViewActivity.this,
                            EasyWebViewActivity.this.webview, "weibo.css");
                } else if (url.contains("m.miaopai.com")) {
                    WebViewUtils.injectCSS(EasyWebViewActivity.this,
                            EasyWebViewActivity.this.webview, "miaopai.css");
                }
            }
        });
        this.webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                EasyWebViewActivity.this.webviewPb.setProgress(progress);
                setProgress(progress * PROGRESS_RATIO);
                if (progress >= 80) {
                    EasyWebViewActivity.this.webviewPb.setVisibility(View.GONE);
                } else {
                    EasyWebViewActivity.this.webviewPb.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @SuppressLint("SetJavaScriptEnabled") private void enableJavascript() {
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }


    private void enableCaching() {
        this.webview.getSettings().setAppCachePath(getFilesDir() + getPackageName() + "/cache");
        this.webview.getSettings().setAppCacheEnabled(true);
        this.webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
    }


    private void enableAdjust() {
        this.webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.webview.getSettings().setLoadWithOverviewMode(true);
    }


    private void zoomedOut() {
        this.webview.getSettings().setLoadWithOverviewMode(true);
        this.webview.getSettings().setUseWideViewPort(true);
        this.webview.getSettings().setSupportZoom(true);
    }


    private String getUrl() {
        return IntentUtils.getStringExtra(this.getIntent(), EXTRA_URL);
    }


    private String getUrlTitle() {
        return IntentUtils.getStringExtra(this.getIntent(), EXTRA_TITLE);
    }


    /**
     * For gank api
     *
     * @return tyep
     */
    private String getGankType() {
        return IntentUtils.getStringExtra(this.getIntent(), EXTRA_GANK_TYPE);
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        if (this.webview != null) this.webview.destroy();
    }


    /**
     * Take care of calling onBackPressed() for pre-Eclair platforms.
     *
     * @param keyCode keyCode
     * @param event event
     */
    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 优先拦截没gankType 和 不是video类型的
            if (this.getGankType() == null) {
                this.keyBackProcessScreenLandscape();
            } else if (GankTypeDict.urlType2TypeDict.get(this.getGankType()) != GankType.video) {
                this.keyBackProcessScreenLandscape();
            } else {
                if (this.goBack) {
                    this.finish();
                } else {
                    this.goBack = true;
                    Message msg = this.mHandler.obtainMessage();
                    msg.what = MSG_WHAT_RESET_GO_BACK;
                    this.mHandler.sendMessageDelayed(msg, RESET_GO_BACK_INTERVAL);
                    ToastUtils.show(this, this.getString(R.string.common_go_back_tip),
                            ToastUtils.LENGTH_SHORT);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 非视频页面 并 横屏的时候 的细节处理
     */
    private void keyBackProcessScreenLandscape() {
        if (this.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            this.switchScreenConfiguration(null);
        } else {
            if (this.webview.canGoBack()) {
                this.webview.goBack();
            } else {
                this.finish();
            }
        }
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
