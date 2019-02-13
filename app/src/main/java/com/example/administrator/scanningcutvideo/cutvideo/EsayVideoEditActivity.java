package com.example.administrator.scanningcutvideo.cutvideo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.esay.ffmtool.FfmpegTool;
import com.example.administrator.scanningcutvideo.LogUtil;
import com.example.administrator.scanningcutvideo.R;
import com.example.administrator.scanningcutvideo.ToastUtil;
import com.example.administrator.scanningcutvideo.base.BaseWeakAsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.getExternalStorageDirectory;


/**
 * 仿微信10秒小视频编辑
 */
public class EsayVideoEditActivity extends AppCompatActivity implements View.OnClickListener, RangeBar.OnRangeBarChangeListener, FfmpegTool.VideoResult {

    protected static final int CUT_VIDEO_REQUEST_CODE = 1006;                                       //剪切视频返回的结果

    public static final String PATH = "path";
    private static final String TAG = "----->EsayVideoEditActivity";
    private final int IMAGE_NUM = 10;//每一屏图片的数量


    public RecyclerView recyclerview;
    public RangeBar rangeBar;
    public FrameLayout fram;
    public VideoView uVideoView;


    private String videoPath;
    private String parentPath;
    private long videoTime;
    private Adapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private int imagCount = 0;//整个视频要解码图片的总数量


    FfmpegTool ffmpegTool;

    private int firstItem = 0;//recycleView当前显示的第一项
    private int lastItem = 0;//recycleView当前显示的最后一项
    private int leftThumbIndex = 0;//滑动条的左端
    private int rightThumbIndex = IMAGE_NUM;//滑动条的右端
    private int startTime, endTime = IMAGE_NUM;//裁剪的开始、结束时间
    private String videoResutlDir;//视频裁剪结果的存放目录
    private String videoResutl;
//    ExecutorService executorService = Executors.newFixedThreadPool(3);

    private Button btn_clip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);


        recyclerview = findViewById(R.id.recyclerview);
        rangeBar = findViewById(R.id.rangeBar);
        fram = findViewById(R.id.fram);
        uVideoView = findViewById(R.id.uVideoView);

        btn_clip = findViewById(R.id.btn_clip);
        btn_clip.setOnClickListener(this);


        videoPath = getIntent().getStringExtra(PATH);
        LogUtil.logDebug(TAG, "videoPath:" + videoPath);
        if (!new File(videoPath).exists()) {
            ToastUtil.showToastSHORT("视频文件不存在");
            finish();
        }
        String str = "temp" + System.currentTimeMillis() / 1000;
        parentPath = getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "briefstore" + File.separator + str + File.separator;
        videoResutlDir = getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "briefstore" + File.separator + "clicp";
        File file = new File(parentPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        rangeBar.setmTickCount(IMAGE_NUM + 1);
        videoTime = UIUtil.getVideoDuration(videoPath);
        LogUtil.logDebug(TAG, "videoTime:" + videoTime);
        ffmpegTool = FfmpegTool.getInstance(this);
        ffmpegTool.setImageDecodeing(new FfmpegTool.ImageDecodeing() {
            @Override
            public void sucessOne(String s, int i) {
                adapter.notifyItemRangeChanged(i, 1);
            }
        });
        initView();
        initData();
    }

    private void initView() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(this, getDataList(videoTime));
        adapter.setParentPath(parentPath);
        adapter.setRotation(UIUtil.strToFloat(UIUtil.getVideoInf(videoPath)));
        recyclerview.setAdapter(adapter);
        recyclerview.addOnScrollListener(onScrollListener);
        rangeBar.setOnRangeBarChangeListener(this);//设置滑动条的监听
        uVideoView.setVideoPath(videoPath);
        uVideoView.start();
    }

    /**
     * 第一次解码，先解码两屏的图片
     */
    private void initData() {
        File parent = new File(parentPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }
        ToastUtil.showToastLONG("解码中,请稍等");

        int[] arr = new int[]{0, 2 * IMAGE_NUM};

        new DecodAsyncTask(this).execute(arr);

//        runImagDecodTask(0, 2 * IMAGE_NUM);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 101;
                handler.handleMessage(message);
            }
        }, 5000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101:

                    btn_clip.setOnClickListener(EsayVideoEditActivity.this);

                    break;
            }
        }
    };

    /**
     * 视频压缩
     *
     * @param view
     */
    public void click2(View view) {
        File file = new File(videoResutl);
        if (file.exists()) {
            ToastUtil.showToastSHORT("开始压缩，过程可能比较漫长");
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    //压缩后视频的保存路径
//                    String path = Environment.getExternalStorageDirectory().getAbsolutePath()
//                            + File.separator + "briefstore" + File.separator + "compress";
//                    File file1 = new File(path);
//                    if (!file1.exists()) {
//                        file1.mkdirs();
//                    }
//                    ffmpegTool.compressVideo(videoResutl, path + File.separator, 3, new FfmpegTool.VideoResult() {
//                        @Override
//                        public void clipResult(int i, String s, String s1, boolean b, int i1) {
//                            String result = "压缩完成";
//                            if (!b) {
//                                result = "压缩失败";
//                            }
//                            Log.i("click2", "s:" + s);//压缩前的视频
//                            Log.i("click2", "s1:" + s1);//压缩后的视频
//                            ToastUtil.showToastSHORT(result);
//                        }
//                    });
//                }
//            });
        } else {
            ToastUtil.showToastSHORT("未找到裁剪后的视频");
        }
    }


//    /**
//     * 裁剪
//     *
//     * @param view
//     */
//    public void onclick(View view) {

//        uVideoView.stopPlayback();
//        File file = new File(videoResutlDir);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        ToastUtil.showToastSHORT("开始裁剪视频");
//
//
//        String video = videoResutlDir + File.separator + "clip" + System.currentTimeMillis() / 1000 + ".mp4";
//        if (!TextUtils.isEmpty(video)) {
//
//            btn_clip.setEnabled(false);
//
//            new ClipAsyncTask(this).execute(video);
//
//        } else {
//            ToastUtil.showToastSHORT("无效的视频！");
//            onFinish();
//        }

//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clip:


                uVideoView.stopPlayback();
                File file = new File(videoResutlDir);
                if (!file.exists()) {
                    file.mkdirs();
                }
                ToastUtil.showToastSHORT("开始裁剪视频");


                String video = videoResutlDir + File.separator + "clip" + System.currentTimeMillis() / 1000 + ".mp4";
                if (!TextUtils.isEmpty(video)) {

                    btn_clip.setOnClickListener(null);

                    new ClipAsyncTask(this).execute(video);

                } else {
                    ToastUtil.showToastSHORT("无效的视频！");
                    onFinish();
                }


                break;
        }
    }


    /**
     * 根据视频的时长，按秒分割成多个data先占一个位置
     *
     * @return
     */
    public List<Data> getDataList(long videoTime) {
        List<Data> dataList = new ArrayList<>();
        int seconds = (int) (videoTime / 1000);
        for (imagCount = 0; imagCount < seconds; imagCount++) {
            dataList.add(new Data(imagCount, "temp" + imagCount + ".jpg"));
        }
        return dataList;
    }

    /**
     * rangeBar 滑动改变时监听，重新计算时间
     *
     * @param rangeBar
     * @param leftThumbIndex
     * @param rightThumbIndex
     */
    @Override
    public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
        LogUtil.logDebug(TAG, "leftThumbIndex:" + leftThumbIndex + "___rightThumbIndex:" + rightThumbIndex);
        this.leftThumbIndex = leftThumbIndex;
        this.rightThumbIndex = rightThumbIndex;
        calStartEndTime();
    }

    /**
     * 计算开始结束时间
     */
    private void calStartEndTime() {
        int duration = rightThumbIndex - leftThumbIndex;
        startTime = firstItem + leftThumbIndex;
        endTime = startTime + duration;
        //此时可能视频已经结束，若已结束重新start
        if (uVideoView != null) {
            if (!uVideoView.isPlaying()) {
                uVideoView.start();
            }
            //把视频跳转到新选择的开始时间
            uVideoView.seekTo(startTime * 1000);
        }
    }


    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LogUtil.logDebug(TAG, "onScrollStateChanged :" + newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                firstItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastItem = linearLayoutManager.findLastVisibleItemPosition();
                List<Data> dataList = adapter.getDataList();
                for (int i = firstItem; i <= lastItem; i++) {
                    if (!UIUtil.isFileExist(parentPath + dataList.get(i).getImageName())) {
                        LogUtil.logDebug(TAG, "not exist :" + i);
//                        runImagDecodTask(i, lastItem - i + 1);

                        int[] arr = new int[]{i, lastItem - i + 1};

                        new DecodAsyncTask(EsayVideoEditActivity.this).execute(arr);

                        break;
                    }
                }
            }
            calStartEndTime();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    /**
     * 运行一个图片的解码任务
     *
     * @param start 解码开始的视频时间 秒
     * @param count 一共解析多少张
     */
    private void runImagDecodTask(final int start, final int count) {
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                ffmpegTool.decodToImageWithCall(videoPath, parentPath, start, count);
//            }
//        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {//获取到图片总的显示范围的大小后，设置每一个图片所占有的宽度
            adapter.setImagWidth(rangeBar.getMeasuredWidth() / IMAGE_NUM);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (uVideoView != null) {
            uVideoView.pause();
        }

    }

    @Override
    public void onBackPressed() {
        onFinish();
    }

    @Override
    protected void onDestroy() {
        onFinish();
        super.onDestroy();
    }


    private void onFinish() {

        //最后不要忘了删除这个临时文件夹 parentPath
        //不然时间长了会在手机上生成大量不用的图片，该activity销毁后这个文件夹就用不到了
        //如果内存大，任性不删也可以


//        if (executorService != null) {
//            executorService.shutdown();
//        }

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        if (uVideoView != null) {
            if (uVideoView.isPlaying()) {
                uVideoView.stopPlayback();
            }
            uVideoView.clearFocus();
            uVideoView = null;
        }

        finish();
    }

    @Override
    public void clipResult(int i, String s, String s1, boolean b, int i1) {
        LogUtil.logDebug(TAG, "--->clipResult i = " + i + " ---> s = " + s + " ---> s1 = " + s1 + " ---> b = " + b + " ---> i1 = " + i1);
        String re = "裁剪视频完成";
        if (!b) {
            re = "裁剪视频失败";
            ToastUtil.showToastSHORT(re);

        } else {

            videoResutl = s1;
            ToastUtil.showToastSHORT(re);
            String cut_video_path = EsayVideoEditActivity.this.getResources().getString(R.string.cut_video_path);
            EsayVideoEditActivity.this.setResult(CUT_VIDEO_REQUEST_CODE, new Intent().putExtra(cut_video_path, videoResutl));
            onFinish();
        }
    }


    /**
     * 剪裁视频  String 输出的路径
     */
    public class ClipAsyncTask extends BaseWeakAsyncTask<String, Void, String, Activity> {


        public ClipAsyncTask(Activity activity) {
            super(activity);
        }

        @Override
        protected void onPreExecute(Activity activity) {

        }

        @Override
        protected String doInBackground(Activity activity, String... string) {

            if (activity != null && !activity.isFinishing()) {

                String outPath = "";

//                int result = Integer.MIN_VALUE;

                try {

                    String video = string[0];

                    if (!TextUtils.isEmpty(video)) {

//                        result = ffmpegTool.clipVideo(videoPath, video, startTime, endTime - startTime, 2, EsayVideoEditActivity.this);

                        outPath = VideoClipUtils.clip(videoPath, video, startTime, endTime);
                    }

                } catch (Exception e) {
                    LogUtil.logDebug(TAG, "--->ClipAsyncTask = " + e.getMessage());
                    btn_clip.setOnClickListener(null);
                }

                return outPath;
            } else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(Activity activity, String outPath) {
            if (activity != null && !activity.isFinishing()) {
                LogUtil.logDebug(TAG, "--->outPath = " + outPath);
                LogUtil.logDebug(TAG, "--->startTime = " + startTime + " --->endTime = " + endTime);
                ToastUtil.showToastSHORT("剪切视频完成！");

                long videoDuration = UIUtil.getVideoDuration(outPath);

                LogUtil.logDebug(TAG, "--->videoDuration = " + videoDuration);
//                ToastUtil.showToastSHORT("--->videoDuration = " + videoDuration);

                String cut_video_path = EsayVideoEditActivity.this.getResources().getString(R.string.cut_video_path);
                EsayVideoEditActivity.this.setResult(CUT_VIDEO_REQUEST_CODE, new Intent().putExtra(cut_video_path, outPath));
                onFinish();

                btn_clip.setOnClickListener(EsayVideoEditActivity.this);
            }
        }
    }

    /**
     * 解码
     */
    public class DecodAsyncTask extends BaseWeakAsyncTask<int[], Void, Void, Activity> {

        public DecodAsyncTask(Activity activity) {
            super(activity);
        }

        @Override
        protected void onPreExecute(Activity activity) {

        }

        @Override
        protected Void doInBackground(Activity activity, int[]... ints) {
            if (activity != null && !activity.isFinishing()) {

                int[] anInt = ints[0];

                if (anInt != null && anInt.length == 2) {

                    if (ffmpegTool != null) {
                        ffmpegTool.decodToImageWithCall(videoPath, parentPath, anInt[0], anInt[1]);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Activity activity, Void aVoid) {

        }
    }


}
