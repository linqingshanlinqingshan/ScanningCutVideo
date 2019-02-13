package com.example.administrator.retrofitmvp;

import android.app.Activity;
import android.text.TextUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public abstract class RxObserver<T extends Object> implements Observer<T> {

    private Activity mActivity;
    public LoadingDialog loadingdialog;
    private String message;
    public boolean isShowDialog = true;

    public RxObserver(Activity activity) {
        this.mActivity = activity;
    }

    public RxObserver setShowDialog(boolean isShowDialog) {
        this.isShowDialog = isShowDialog;
        return this;
    }

    public RxObserver setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if (isShowDialog) {
            showLoadingDialog(TextUtils.isEmpty(message) ? "正在访问..." : message);
        }
    }

    @Override
    public void onNext(@NonNull T object) {
        if (isShowDialog) {
            cancelLoadingDialog();
        }
        _onNext(object);
    }

    public abstract void _onNext(T t);

    @Override
    public void onError(@NonNull Throwable e) {
        if (isShowDialog) {
            cancelLoadingDialog();
        }
        e.printStackTrace();
        if (e instanceof SocketTimeoutException) {
            ToastUtil.showToastSHORT(TextUtils.isEmpty(_onMessage()) ? "网络不好哟，请稍后重试..." : _onMessage());
        } else if (NetStateUtil.getInstance().setNetStatuTIp(mActivity)) {
            ToastUtil.showToastSHORT(TextUtils.isEmpty(_onMessage()) ? "网络不可用..." : _onMessage());
        } else if (e instanceof ApiException) {
            ToastUtil.showToastSHORT(TextUtils.isEmpty(e.getMessage()) ? (TextUtils.isEmpty(_onMessage()) ? "请求失败" : _onMessage()) : e.getMessage());
        } else {
            ToastUtil.showToastSHORT(TextUtils.isEmpty(_onMessage()) ? "请求失败，请稍后再试..." : _onMessage());
        }
        _onError();
    }

    public abstract String _onMessage();

    public abstract void _onError();

    @Override
    public void onComplete() {
        if (isShowDialog) {
            cancelLoadingDialog();
        }
    }

    /*** 显示加载对话框 */
    public void showLoadingDialog(String text) {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        if (loadingdialog != null) {
            loadingdialog.setText(text);
            if (!loadingdialog.isShowing()) {
                loadingdialog.show();
            }
        } else {
            loadingdialog = new LoadingDialog(mActivity, R.style.Style_LoadingDialog, text);
            loadingdialog.show();
        }
    }

    /*** 取消加载对话框 */
    public void cancelLoadingDialog() {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        if (loadingdialog != null && loadingdialog.isShowing()) {
            loadingdialog.dismiss();
        }
    }

}
