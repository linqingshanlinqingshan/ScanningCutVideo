package com.example.administrator.scanningcutvideo;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.administrator.scanningcutvideo.base.BaseWeakAsyncTask;
import com.example.administrator.scanningcutvideo.cutvideo.EsayVideoEditActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * 微见证-显示本地视频列表
 */
public class ShowLocalVideoListActivity extends AppCompatActivity implements ShowLocalVideoListAdapter.OnClickSelectVideoListener {

    private static final String TAG = "----->ShowLocalVideoListActivity";

    protected static final int SHOW_LOCAL_VIDEO_REQUEST_CODE = 1005;                                //显示本地视频
    protected static final int CUT_VIDEO_REQUEST_CODE = 1006;                                       //剪切视频返回的结果

    public RecyclerView recycler_view;
    protected ProgressDialog waitingDialog;  //扫描手机的本地视频

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_local_video_layout);

        recycler_view = findViewById(R.id.recycler_view);

        new ScanningLocalVideoAsyncTask(this).execute();

    }

    /**
     * 扫描手机的本地视频
     */
    private void showWaitingDialog() {
        /* 等待Dialog具有屏蔽其他控件的交互能力
         * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
         * 下载等事件完成后，主动调用函数关闭该Dialog
         */
        waitingDialog = new ProgressDialog(this);
        waitingDialog.setTitle("正在扫描");
        waitingDialog.setMessage("请稍后...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
    }

    @Override
    public void onClickSelectVideoListener(String name, String path) {

        long ms = 10000;

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);        //播放时长单位为毫秒

        LogUtil.logDebug(TAG, "--->duration = " + duration);

        boolean numeric = OtherUtils.isNumeric(duration);
        if (numeric) {
            long l = Long.parseLong(duration);
            if (l <= ms) {
                LogUtil.logDebug(TAG, "--->duration l = " + l);
                onFinish(path);
            } else {
                String substring = path.substring(path.length() - 4);
                LogUtil.logDebug(TAG, "--->substring = " + substring);

                if (substring.equals(".mp4")) {
                    //剪切视频
                    cutVideoTenSecondsPermissions(name, path);
                } else {
                    ToastUtil.showToastSHORT("抱歉，目前只能剪切mp4格式的视频！");
                }
            }
        }
    }


    /**
     * 剪切 10s 视频 权限
     *
     * @param name
     * @param path
     */
    private void cutVideoTenSecondsPermissions(final String name, final String path) {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            Toast.makeText(ShowLocalVideoListActivity.this, "请您先允许sd卡读写权限！", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            cutVideoTenSeconds(name, path);
                        }
                    }
                });
    }

    /**
     * 剪切 10s 视频
     *
     * @param name
     * @param path
     */
    private void cutVideoTenSeconds(String name, String path) {
        Intent intent = new Intent();
        intent.putExtra(EsayVideoEditActivity.PATH, path);
        intent.setClass(this, EsayVideoEditActivity.class);
        startActivityForResult(intent, CUT_VIDEO_REQUEST_CODE);
    }


    /**
     * 扫描手机的视频文件
     */
    protected class ScanningLocalVideoAsyncTask extends BaseWeakAsyncTask<Void, Void, ArrayList<ScanningLocalVideoUtils.MediaBean>, Activity> {

        public ScanningLocalVideoAsyncTask(Activity activity) {
            super(activity);
        }

        @Override
        protected void onPreExecute(Activity activity) {
            if (activity != null && !activity.isFinishing()) {
                showWaitingDialog();
            }
        }

        @Override
        protected ArrayList<ScanningLocalVideoUtils.MediaBean> doInBackground(Activity activity, Void... voids) {
            if (activity != null && !activity.isFinishing()) {

                ArrayList<ScanningLocalVideoUtils.MediaBean> videoList = ScanningLocalVideoUtils.getVideoFile(new ArrayList<ScanningLocalVideoUtils.MediaBean>(), Environment.getExternalStorageDirectory());

                if (videoList != null) {
                    int size = videoList.size();
                    for (int i = 0; i < size; i++) {

                        LogUtil.logDebug(TAG, "---> ScanningLocalVideoAsyncTask videoList name = " + videoList.get(i).getMediaName());

                        LogUtil.logDebug(TAG, "---> ScanningLocalVideoAsyncTask videoList path = " + videoList.get(i).getPath());
                    }
                    LogUtil.logDebug(TAG, "---> ScanningLocalVideoAsyncTask videoList size = " + size);

                    return videoList;
                } else {

                    return new ArrayList<ScanningLocalVideoUtils.MediaBean>();
                }
            } else {
                return new ArrayList<ScanningLocalVideoUtils.MediaBean>();
            }
        }

        @Override
        protected void onPostExecute(Activity activity, ArrayList<ScanningLocalVideoUtils.MediaBean> videoList) {
            if (activity != null && !activity.isFinishing()) {

                List<Bitmap> bitmapList = new ArrayList<>();

                for (int i = 0; i < videoList.size(); i++) {
                    bitmapList.add(getVideoThumbnail(videoList.get(i).getPath(), ScreenUtil.dp2px(activity, 100), ScreenUtil.dp2px(activity, 100), MediaStore.Images.Thumbnails.MINI_KIND));
                }

                if (waitingDialog != null) {
                    waitingDialog.dismiss();
                    waitingDialog = null;
                }

                showList(videoList, bitmapList);
            }
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

    /**
     * 显示列表
     *
     * @param videoList
     */
    private void showList(ArrayList<ScanningLocalVideoUtils.MediaBean> videoList, List<Bitmap> bitmapList) {

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        ShowLocalVideoListAdapter adapter = new ShowLocalVideoListAdapter(videoList, bitmapList, this, this);
        recycler_view.setLayoutManager(staggeredGridLayoutManager);
        recycler_view.setAdapter(adapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CUT_VIDEO_REQUEST_CODE) {
            if (data != null) {
                String cut_video_path = getResources().getString(R.string.cut_video_path);
                LogUtil.logDebug(TAG, "--->cut_video_path = " + cut_video_path);

                String path = data.getStringExtra(cut_video_path);

                LogUtil.logDebug(TAG, "--->path = " + path);

                if (!TextUtils.isEmpty(path)) {
                    onFinish(path);
                } else {
                    ToastUtil.showToastSHORT("视频剪切失败，请重新剪切！");
                }
            }
        }
    }

    private void onFinish(String path) {
        setResult(SHOW_LOCAL_VIDEO_REQUEST_CODE, new Intent().putExtra("path", path));
        finish();
    }
}
