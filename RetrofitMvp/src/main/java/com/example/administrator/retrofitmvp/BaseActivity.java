package com.example.administrator.retrofitmvp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "----->BaseActivity";

    protected android.content.Context mContext;

    protected Activity mActivity;

    // 通用标题栏布局
    protected PercentFrameLayout layout_titleBar;
    // 内容布局
    protected PercentFrameLayout layout_content;

    // 通用遮罩层布局
    protected PercentLinearLayout layout_maskLayer;
    protected PercentLinearLayout layout_leftView;
    protected PercentRelativeLayout layout_rightView;

    // 通用访问记录
    protected ImageView layout_leftMore;
    protected ImageView layout_rightRecord;
    protected View titleBar;

    protected RelativeLayout layout_maskLayer_record;

    protected ImageView profile_image;
    protected TextView tv_name;
    protected TextView tv_addressAndRole;


    protected ExpandableListView layout_ExpandableListView;

    private InputMethodManager imm;
    protected ImmersionBar mImmersionBar;

    protected ImageView titleBar_leftMore;
    protected ImageView titleBar_rightRecord;
    protected TextView titleBar_title;

    protected ImageView layout_recordReduce;
    protected ImageView layout_recordAdd;
    protected TextView layout_recordCount;
    protected TextView layout_startRecord;

    protected TextView layout_recordTime;
    protected TextView layout_recordingCount;
    protected TextView layout_addInfo;
    protected TextView layout_stopRecord;

    protected LinearLayout layout_recordStart;
    protected LinearLayout layout_recording;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_base);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // 将activity加入AppManager
        AppMgr.getInstance().addAct(this);

        setTranslucentStatus(true);

        mContext = this;

        initImmersionBar();

        // 初始化所有Base控件
        initBaseViews();


        // 初始化content内容和具体业务操作
        mActivity = initViews();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventType eventType) {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }


    /**
     * 加载TitleBar资源文件(如返回0则默认加载titlebar.xml)
     *
     * @return
     */
    protected abstract int loadTitleBarXml();

    /**
     * 初始化TitleBar内容 要隐藏titleBar 找 fl_title_bar
     */
    protected abstract void initTitleBar();

    /**
     * 加载content资源文件
     *
     * @return
     */
    protected abstract int loadContentXml();

    /**
     * 初始化控件和具体业务操作
     */
    protected abstract Activity initViews();


    /**
     * 将BaseActivity的控件都初始化
     */
    public void initBaseViews() {
        // 通用遮罩层布局
        layout_maskLayer = findViewById(R.id.layout_maskLayer);
        layout_leftView = findViewById(R.id.layout_leftView);
        layout_rightView = findViewById(R.id.layout_rightView);

        layout_leftMore = findViewById(R.id.layout_leftMore);
        layout_rightRecord = findViewById(R.id.layout_rightRecord);

        layout_maskLayer_record = findViewById(R.id.layout_maskLayer_record);

        layout_recordStart = findViewById(R.id.layout_recordStart);
        layout_recording = findViewById(R.id.layout_recording);

        layout_recordReduce = findViewById(R.id.layout_recordReduce);
        layout_recordAdd = findViewById(R.id.layout_recordAdd);
        layout_recordCount = findViewById(R.id.layout_recordCount);
        layout_startRecord = findViewById(R.id.layout_startRecord);

        layout_recordTime = findViewById(R.id.layout_recordTime);
        layout_recordingCount = findViewById(R.id.layout_recordingCount);
        layout_addInfo = findViewById(R.id.layout_addInfo);
        layout_stopRecord = findViewById(R.id.layout_stopRecord);

        profile_image = findViewById(R.id.profile_image);
        tv_name = findViewById(R.id.tv_name);
        tv_addressAndRole = findViewById(R.id.tv_addressAndRole);

        layout_ExpandableListView = findViewById(R.id.layout_ExpandableListView);

        // 获取titleBar下面的layout资源(titlebar的根布局)
        layout_titleBar = findViewById(R.id.layout_titleBar);
        ViewGroup.LayoutParams titBarP = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtil.sp2px(this, 50));

        // 表示titlebar使用默认的
        if (loadTitleBarXml() == 0) {
            titleBar = LayoutInflater.from(this).inflate(R.layout.common_title_bar, null);
            layout_titleBar.addView(titleBar, titBarP);

            // 执行findViewById(...)和加载content资源文件
            titleBar_leftMore = titleBar.findViewById(R.id.titleBar_leftMore);
            titleBar_rightRecord = titleBar.findViewById(R.id.titleBar_rightRecord);
            titleBar_title = titleBar.findViewById(R.id.titleBar_title);


            initTitleBar();

            // 布局文件直接写titlebar
        } else if (loadTitleBarXml() == -1) {
            layout_titleBar = findViewById(R.id.layout_titleBar);
            layout_titleBar.setVisibility(View.GONE);
            initTitleBar();
            // 表示titlebar使用自定义的
        } else {
            titleBar = LayoutInflater.from(this).inflate(loadTitleBarXml(), null);
            layout_titleBar.addView(titleBar, titBarP);
            initTitleBar();
        }

        // 将子视图加载到目视图中
        layout_content = findViewById(R.id.layout_content);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        // 获取titleBar下面的layout资源
        View childView = LayoutInflater.from(this).inflate(loadContentXml(), null);

        layout_content.addView(childView, layoutParams);


    }


    /**
     * 设置沉浸式
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }


    public void finish() {
        super.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.imm = null;
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        AppMgr.getInstance().closeAct(this);

    }

}
