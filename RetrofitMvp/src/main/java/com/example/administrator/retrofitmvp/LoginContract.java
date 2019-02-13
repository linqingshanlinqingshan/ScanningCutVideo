package com.example.administrator.retrofitmvp;


import java.util.List;

public interface LoginContract {

    interface View extends BaseView {
        void refreshView(List<ShopBrandAdverBean.DataBean> dataBeanList,String name, String password);
    }

    interface Presenter extends BasePresenter<View> {
        void login(String account, String password);
    }

}
