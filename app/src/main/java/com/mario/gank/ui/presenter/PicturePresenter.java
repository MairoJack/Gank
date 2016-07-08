package com.mario.gank.ui.presenter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.mario.gank.R;
import com.mario.gank.core.mvp.BasePresenter;
import com.mario.gank.ui.presenter.ivview.PictureView;
import com.mario.gank.utils.DeviceUtils;
import com.mario.gank.utils.RxUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import rx.Observable;
import rx.Subscriber;


public class PicturePresenter extends BasePresenter<PictureView> {

    public Observable<String> getSavePictureObservable(
            @NonNull final GlideBitmapDrawable glideBitmapDrawable,
            @NonNull final Context context, @NonNull final Application application) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override public void call(Subscriber<? super String> subscriber) {
                try {
                    String dirPath = DeviceUtils.createAPPFolder(
                            context.getString(R.string.app_name), application);
                    File downloadFile = new File(new File(dirPath),
                            UUID.randomUUID().toString().replace("-", "") + ".jpg");
                    if (!downloadFile.exists()) {
                        File parent = downloadFile.getParentFile();
                        if (parent != null && !parent.exists()) parent.mkdirs();
                    }
                    FileOutputStream output = new FileOutputStream(downloadFile);
                    glideBitmapDrawable.getBitmap()
                                       .compress(Bitmap.CompressFormat.JPEG, 100, output);
                    output.close();

                    // 更新相册
                    Uri uri = Uri.fromFile(downloadFile);
                    Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                    context.sendBroadcast(scannerIntent);
                    subscriber.onNext(downloadFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).compose(RxUtils.applyIOToMainThreadSchedulers());
    }


    public void downloadPicture(
            @NonNull final GlideBitmapDrawable glideBitmapDrawable,
            @NonNull final Context context, @NonNull final Application application) {
        this.mCompositeSubscription.add(
                this.getSavePictureObservable(glideBitmapDrawable, context, application)
                    .subscribe(new Subscriber<String>() {
                        @Override public void onCompleted() {
                            PicturePresenter.this.mCompositeSubscription.remove(this);
                        }


                        @Override public void onError(Throwable e) {
                            if (PicturePresenter.this.getMvpView() != null) {
                                PicturePresenter.this.getMvpView().onFailure(e);
                            }
                        }


                        @Override public void onNext(String s) {
                            if (PicturePresenter.this.getMvpView() != null) {
                                PicturePresenter.this.getMvpView().onDownloadSuccess(s);
                            }
                        }
                    }));
    }


    public void sharePicture(
            @NonNull final GlideBitmapDrawable glideBitmapDrawable,
            @NonNull final Context context, @NonNull final Application application) {
        this.mCompositeSubscription.add(
                this.getSavePictureObservable(glideBitmapDrawable, context, application)
                    .subscribe(new Subscriber<String>() {
                        @Override public void onCompleted() {
                            PicturePresenter.this.mCompositeSubscription.remove(this);
                        }


                        @Override public void onError(Throwable e) {
                            if (PicturePresenter.this.getMvpView() != null) {
                                PicturePresenter.this.getMvpView().onFailure(e);
                            }
                        }


                        @Override public void onNext(String s) {
                            if (PicturePresenter.this.getMvpView() != null) {
                                Uri uri = Uri.parse("file://" + s);
                                PicturePresenter.this.getMvpView().onShare(uri);
                            }
                        }
                    }));
    }
}
