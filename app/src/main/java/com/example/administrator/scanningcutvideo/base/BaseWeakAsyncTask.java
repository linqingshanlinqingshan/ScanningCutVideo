package com.example.administrator.scanningcutvideo.base;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

/**
 *
 * 使用弱引用，当 Activity 或者 Fragment 销毁而访问网络数据没有停止时，访问网络完成就销毁引用
 */

public abstract class BaseWeakAsyncTask<Params,Progress,Result,WeakTarget> extends AsyncTask<Params,Progress,Result>{

    //外部类弱引用
    protected final WeakReference<WeakTarget> mTarget;

    public BaseWeakAsyncTask(WeakTarget target){
        mTarget = new WeakReference<WeakTarget>(target);
    }

    @Override
    protected void onPreExecute() {
        final WeakTarget target = mTarget.get();
        if(target != null){
            this.onPreExecute(target);
        }
    }

    @Override
    protected Result doInBackground(Params... params) {

        final WeakTarget target = mTarget.get();
        if(target != null){
            return this.doInBackground(target,params);
        }else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        final WeakTarget target = mTarget.get();
        if(target != null){
            this.onPostExecute(target,result);
        }
    }

    protected abstract void onPreExecute(WeakTarget target);
    protected abstract Result doInBackground(WeakTarget target,Params... params);
    protected abstract void onPostExecute(WeakTarget target,Result result);
}


//        if (context == null && context.isFinishing()) {
//
//        Log.d(TAG, "------> context 没了,可以结束了 ");
//        return;
//    } else if (context != null && context.isFinishing()) {
//
//        Log.d(TAG, "------> activity有，但是 onDestroy了，可以结束了 ");
//        return;
//    } else if (context != null && !context.isFinishing()) {
//
//        if (TextUtils.isEmpty("")) {
//
//        } else {
//
//        }
//    }