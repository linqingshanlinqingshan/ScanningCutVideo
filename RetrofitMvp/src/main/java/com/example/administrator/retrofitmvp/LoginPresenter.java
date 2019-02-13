package com.example.administrator.retrofitmvp;


import android.app.Activity;

import java.util.List;

/**
 * Description: WelcomePresenter
 * Creator: yxc
 * date: 2016/9/22 13:17
 */
public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {

    private static final String TAG = "----->LoginPresenter";

    @Override
    public void login(final String name, final String password) {
//        Api.getDefault().login(account, password)
//                .compose(RxHelper.<UserBean>handleResult())
//                .subscribe(new RxObserver<UserBean>((Activity)mView) {
//                    @Override
//                    public void _onNext(UserBean userBean) {
//                        mView.refreshView(userBean);
//                    }
//
//                    @Override
//                    public String _onMessage() {
//                        return null;
//                    }
//
//                    @Override
//                    public void _onError() {
//
//                    }
//                });


        Api.getDefault().getShopBrandAdver()
                .compose(RxHelper.<ShopBrandAdverBean>handleResult())
                .subscribe(new RxObserver<ShopBrandAdverBean>((Activity) mView) {
                    @Override
                    public void _onNext(ShopBrandAdverBean shopBrandAdverBean) {
                        if (shopBrandAdverBean != null) {
                            List<ShopBrandAdverBean.DataBean> data = shopBrandAdverBean.getData();
                            mView.refreshView(data, name, password);
                        }

                    }

                    @Override
                    public String _onMessage() {
                        return null;
                    }

                    @Override
                    public void _onError() {

                    }
                });


    }

}
