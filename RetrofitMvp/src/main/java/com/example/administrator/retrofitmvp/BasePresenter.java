package com.example.administrator.retrofitmvp;


public interface BasePresenter<T extends BaseView> {
    void attachView(T view);
    void detachView();
}
