package com.example.administrator.scanningcutvideo;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;


// .setAnimationStyle(R.style.AnimationFromButtom); //从下往上弹出的动画
public abstract class BasePopupWindow extends PopupWindow implements View.OnClickListener {

    private static final String TAG = "----->BasePopupWindow";

    protected Context mContext;
    protected float mShowAlpha = 0.5f;
    protected Drawable mBackgroundDrawable;
    protected View mView;


    public BasePopupWindow(Context context, View view) {
        this.mContext = context;
        this.mView = view;

        LogUtil.logDebug(TAG,"--->BasePopupWindow");

        initBasePopupWindow();
    }

    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);

        LogUtil.logDebug(TAG,"--->setOutsideTouchable");

        if (touchable) {
            if (mBackgroundDrawable == null) {
                mBackgroundDrawable = new ColorDrawable(0x00000000);
            }
            super.setBackgroundDrawable(mBackgroundDrawable);
        } else {
            super.setBackgroundDrawable(null);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {

        LogUtil.logDebug(TAG,"--->setBackgroundDrawable");

        mBackgroundDrawable = background;
        setOutsideTouchable(isOutsideTouchable());
    }

    /**
     * 初始化BasePopupWindow的一些信息
     */
    protected void initBasePopupWindow() {
        setContentView(mView);  //注入view
        setAnimationStyle(android.R.style.Animation_Dialog);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);  //界面的高
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);   //界面的宽
        setOutsideTouchable(true);  //默认设置outside点击无响应  false
        setFocusable(true);

        LogUtil.logDebug(TAG,"--->initBasePopupWindow");
    }


    @Override
    public void setContentView(View contentView) {

        LogUtil.logDebug(TAG,"--->setContentView");

        if (contentView != null) {
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            super.setContentView(contentView);
            addKeyListener(contentView);
//            contentView.setOnKeyListener(new View.OnKeyListener() {
//
//                @Override
//                public boolean onKey(View view, int keyCode, KeyEvent event) {
//                    switch (keyCode) {
//                        case KeyEvent.KEYCODE_BACK:
//
//                            LogUtil.logDebug(TAG, "----> KEYCODE_BACK  dismiss");
//
//                            dismiss();
//
//                            return true;
//                        default:
//                            break;
//                    }
//                    return false;
//                }
//            });
        }
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        showAnimator().start();
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        showAnimator().start();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        showAnimator().start();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        showAnimator().start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dismissAnimator().start();
    }

    /**
     * 窗口显示，窗口背景透明度渐变动画
     */
    protected ValueAnimator showAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, mShowAlpha);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                setWindowBackgroundAlpha(alpha);
            }
        });
        animator.setDuration(360);
        return animator;
    }

    /**
     * 窗口隐藏，窗口背景透明度渐变动画
     */
    protected ValueAnimator dismissAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(mShowAlpha, 1.0f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                setWindowBackgroundAlpha(alpha);
            }
        });
        animator.setDuration(320);
        return animator;
    }

    /**
     * 为窗体添加outside点击事件
     */
    private void addKeyListener(View contentView) {
        if (contentView != null) {

            initView(contentView);
            initListener();

            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);

            setPopupOnKeyListener(contentView);
        }
    }

    /**
     * popup的窗体外点击事件，抽象出去
     */
    public abstract void setPopupOnKeyListener(View contentView);

    /**
     * popup的点击监听设置，抽象出去
     */
    public abstract void initListener();

    /**
     * popup的 find 控件，抽象出去
     */
    public abstract void initView(View contentView);


    /**
     * 控制窗口背景的不透明度
     */
    private void setWindowBackgroundAlpha(float alpha) {
        Window window = ((Activity) getContext()).getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = alpha;
        window.setAttributes(layoutParams);
    }

    /**
     * popup的窗体点击事件，抽象出去
     *
     * @param view
     */
    @Override
    public abstract void onClick(View view);

//    //在 Activity 调用
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {  //点击popup外面时，不消失，activity不分发事件
//        if (popupWindow != null && popupWindow.isShowing()) {
//            return false;
//        }
//        return super.dispatchTouchEvent(event);
//    }

}
