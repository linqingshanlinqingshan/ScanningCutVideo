package com.example.administrator.retrofitmvp;

import android.os.Bundle;



public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements BaseView {

    public T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 初始化Presenter
        // Presenter将activity绑定
        mPresenter = initPresenter();
        if (mPresenter!=null) {
            mPresenter.attachView(this);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null) {
            mPresenter.detachView();
        }
    }

    /**
     * 初始化Presenter
     * @return
     */
    public abstract T initPresenter();

    @Override
    public void showError(String message) {

    }

}
