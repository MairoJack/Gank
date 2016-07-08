
package com.mario.gank.ui.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.mario.gank.R;
import com.mario.gank.core.BaseToolbarActivity;
import com.mario.gank.utils.GlideUtils;

import butterknife.BindView;


public class AboutActivity extends BaseToolbarActivity {

    @BindView(R.id.about_avatar_iv) ImageView aboutAvatarIv;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }


    /**
     * Fill in layout id
     *
     * @return layout id
     */
    @Override protected int getLayoutId() {
        return R.layout.activity_about;
    }


    /**
     * Initialize the view in the layout
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override protected void initViews(Bundle savedInstanceState) {
        this.showBack();
        this.setTitle(this.getString(R.string.about_title));
        GlideUtils.displayCircleHeader(this.aboutAvatarIv, R.mipmap.ic_camnter);
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

    }
}
