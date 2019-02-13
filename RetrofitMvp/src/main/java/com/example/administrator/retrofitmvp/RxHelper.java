package com.example.administrator.retrofitmvp;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Description: Rx 一些巧妙的处理
 */
public class RxHelper {
    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static <T extends BaseResponse> ObservableTransformer<T, T> handleResult() {
        return new ObservableTransformer<T, T>(){
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> observable) {
                return observable.flatMap(new Function<T, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull final T result) throws Exception {
                        if (result.success()) {
                            return Observable.create(new ObservableOnSubscribe<T>() {
                                @Override
                                public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                    try {
                                        LogUtil.logError("响应结果", "响应结果 = " + result.getStatus());
                                        e.onNext(result);
                                        e.onComplete();
                                    } catch (Exception ex) {
                                        e.onError(ex);
                                    }
                                }
                            });
                        } else {
                            return Observable.error(new ApiException(result.getMessage()));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 全部返回结果
     *
     */
    public static <Object> ObservableTransformer<Object, Object> handleAllResult() {
        return new ObservableTransformer<Object, Object>(){
            @Override
            public ObservableSource<Object> apply(@NonNull Observable<Object> observable) {
                return observable.flatMap(new Function<Object, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(@NonNull final Object result) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Object>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                                try {
                                    e.onNext(result);
                                    e.onComplete();
                                } catch (Exception ex) {
                                    e.onError(ex);
                                }
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
