package com.example.administrator.scanningcutvideo;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.VideoView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SelectVideoResourcesPopupWindows.OnClickVideoResourcesPopupListener {

    private static final String TAG = "----->MainActivity";

    TextView tv_button;

    ImageView iv_video_view_preview;

    VideoView vv_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vv_video = findViewById(R.id.vv_video);

        iv_video_view_preview = findViewById(R.id.iv_video_view_preview);

        tv_button = findViewById(R.id.tv_button);
        tv_button.setOnClickListener(this);

    }

//    /**
//     * 防内存泄漏
//     *
//     * @param newBase
//     */
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(new ContextWrapper(newBase) {
//            @Override
//            public Object getSystemService(String name) {
//                if (Context.AUDIO_SERVICE.equals(name)) {
//                    return getApplicationContext().getSystemService(name);
//                }
//                return super.getSystemService(name);
//            }
//        });
//    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_button:

                requestVideo();

                break;
        }
    }

    SelectVideoResourcesPopupWindows selectVideoResourcesPopupWindows;


    /**
     * 添加视频
     */
    protected void requestVideo() {

        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        LogUtil.logDebug(TAG, "--->width = " + width + " --->height = " + height);

        View popupView = LayoutInflater.from(this).inflate(R.layout.select_video_resources_popup, null);

        TextView tv_shooting = popupView.findViewById(R.id.tv_shooting);
        TextView tv_local = popupView.findViewById(R.id.tv_local);

        LinearLayout.LayoutParams layoutParams_img = (LinearLayout.LayoutParams) tv_shooting.getLayoutParams();

        layoutParams_img.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams_img.width = width - 30;

        tv_shooting.setLayoutParams(layoutParams_img);
        tv_shooting.setText("拍摄");

        LinearLayout.LayoutParams layoutParams_video = (LinearLayout.LayoutParams) tv_local.getLayoutParams();

        layoutParams_video.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams_video.width = width - 30;

        tv_local.setLayoutParams(layoutParams_video);
        tv_local.setText("本地");


        selectVideoResourcesPopupWindows = new SelectVideoResourcesPopupWindows(this, popupView, this);
        selectVideoResourcesPopupWindows.showAtLocation(tv_button, Gravity.CENTER, 0, 0);
        selectVideoResourcesPopupWindows.setAnimationStyle(R.style.AnimationFromButtom); //从下往上弹出的动画
        selectVideoResourcesPopupWindows.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (selectVideoResourcesPopupWindows != null) {
                    selectVideoResourcesPopupWindows.dismiss();
                    selectVideoResourcesPopupWindows = null;
                }
            }
        });


    }

    @Override
    public void onClickVideoResourcesPopup(String flag) {

        String shooting = getResources().getString(R.string.shooting);
        String local = getResources().getString(R.string.local);

        if (flag.equals(shooting)) {
            requestShootingPermissions(shooting);
        } else if (flag.equals(local)) {
            requestLocalPermissions(local);
        }
    }

    private void requestLocalPermissions(String local) {
        requestLocal(local);
    }

    protected static final int SHOW_LOCAL_VIDEO_REQUEST_CODE = 1005;                                //显示本地视频

    private void requestLocal(String local) {
        startActivityForResult(new Intent(this, ShowLocalVideoListActivity.class),
                SHOW_LOCAL_VIDEO_REQUEST_CODE);
    }

    private void requestShootingPermissions(final String shooting) {

        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            ToastUtil.showToastLONG("请您先允许相机权限！");
                            return;
                        } else {
                            requestShooting(shooting);
                        }
                    }
                });

    }


    protected static final int SHOOTING_VIDEO_REQUEST_CODE = 1004;                                  //拍摄视频

    /**
     * 拍摄
     *
     * @param shooting
     */
    protected void requestShooting(String shooting) {

        try {

            Uri outputFileUri = null;

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// sd card 可用

                //创建输出文件
                File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".mp4");  //存放在sd卡的根目录下，目前没用，找不到，只是为了传参作用
                outputFileUri = Uri.fromFile(file);

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);     //限制的录制时长 以秒为单位
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024);               //限制视频文件大小 以字节为单位
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);               //设置拍摄的质量0~1
//        intent.putExtra(MediaStore.EXTRA_FULL_SCREEN, false);             // 全屏设置
                startActivityForResult(intent, SHOOTING_VIDEO_REQUEST_CODE);

            } else {// 当前不可用
                ToastUtil.showToastSHORT("当前sd卡不可用！");
            }

        } catch (Exception e) {
            LogUtil.logDebug(TAG, "e:" + e.getMessage());
            LogUtil.logDebug(TAG, "e:" + e);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// sd card 可用

            try {

                File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".mp4");

                Uri uri;
                if (Build.VERSION.SDK_INT >= 24) {
                    uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", file);
                } else {
                    uri = Uri.fromFile(file);
                }

                LogUtil.logError("uri = ", uri.toString());

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);     //限制的录制时长 以秒为单位
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024);               //限制视频文件大小 以字节为单位
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);               //设置拍摄的质量0~1
//        intent.putExtra(MediaStore.EXTRA_FULL_SCREEN, false);             // 全屏设置

                startActivityForResult(intent, SHOOTING_VIDEO_REQUEST_CODE);

            } catch (Exception e) {
                LogUtil.logError("uri e = ", e.getMessage());
                LogUtil.logError("uri e = ", e.toString());
            }

        } else {

        }

    }

    protected String videoPath = null;                                                              //视频可以上传的路径

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogUtil.logDebug(TAG, "--->requestCode = " + requestCode + "--->resultCode = " + resultCode);

        if (requestCode == SHOW_LOCAL_VIDEO_REQUEST_CODE) {                                         //选择本地视频

            videoPath = null;

            if (data != null) {
                videoPath = data.getStringExtra("path");
                LogUtil.logDebug(TAG, "---->选择本地视频 videoPath = " + videoPath);

                if (!TextUtils.isEmpty(videoPath)) {

                    setImageThumbnail(iv_video_view_preview, videoPath);


                    try {

                        vv_video.setVideoPath(videoPath);
                        vv_video.start();

                    } catch (Exception e) {

                    }

                } else {

                }
            }
        } else if (requestCode == SHOOTING_VIDEO_REQUEST_CODE) {                                           //拍摄视频

            videoPath = null;

            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    LogUtil.logDebug(TAG, "---->得到的视频路径 uri = " + uri);                   //拍摄视频可以播放的路径

                    videoPath = uri.getPath();
                    LogUtil.logDebug(TAG, "---->得到的视频路径 uri.getPath = " + videoPath);     //拍摄视频可以上传的路径

                    File externalStorageDirectory = Environment.getExternalStorageDirectory();      //拍摄视频根目录
                    LogUtil.logDebug(TAG, "---->得到的视频路径 Environment.getExternalStorageDirectory() = " + externalStorageDirectory);

                    setImageThumbnail(iv_video_view_preview, videoPath);


                    try {

                        vv_video.setVideoURI(uri);
                        vv_video.start();

                    } catch (Exception e) {

                    }

                } else {
                    LogUtil.logDebug(TAG, "---->得到的视频路径 uri = null");
                }
            } else {

            }
        }
    }


    /**
     * 设置视频缩略图
     *
     * @param imageView
     * @param videoPath
     */
    protected void setImageThumbnail(ImageView imageView, String videoPath) {

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        int width_ = dm.widthPixels;
        int height_ = dm.heightPixels;

        Bitmap bitmap = getVideoThumbnail(videoPath, width_, ScreenUtil.dp2px(this, 197), MediaStore.Images.Thumbnails.MINI_KIND);
        if (bitmap == null) {
            imageView.setBackground(getResources().getDrawable(R.mipmap.photo_pictures_no));
        } else {
            imageView.setImageBitmap(bitmap);
        }

    }


    /**
     * 获取视频缩略图
     *
     * @param videoPath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    protected Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vv_video != null && vv_video.isPlaying()) {
            vv_video.stopPlayback();
            vv_video = null;
        }
    }
}
