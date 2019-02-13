package com.example.administrator.retrofitmvp;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Description: 用于生成ApiService实例，以及对Retrofit做相关配置
 */
public class Api {

    public final static int CODE_SUCESS = 200; // 请求成功
    public final static String CODE_UNAUTHORIZED = "401"; // 授权失败
    public final static String CODE_FORBIDDEN = "403"; // 被禁止
    public final static String CODE_NOTFOUND = "404"; // 地址错误

    private static final long DEFAULT_TIMEOUT = 60;
    private static ApiService SERVICE;
    private static boolean isDefault = true;

    public static ApiService getDefault() {
        if (SERVICE == null || !isDefault) {
            synchronized (Api.class) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .addNetworkInterceptor(
                                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addInterceptor(new HeaderInterceptor())
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)//错误重连
                        .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                        .build();

                SERVICE = new Retrofit.Builder()
                        .baseUrl(BriefStoreConfig.URL_BASE)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())     //设置使用Gson解析(记得加入依赖)
                        .client(client)
                        .build().create(ApiService.class);
            }
        }
        isDefault = true;
        return SERVICE;
    }

    public static ApiService getDefaultV3() {
        if (SERVICE == null || isDefault) {
            synchronized (Api.class) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .addNetworkInterceptor(
                                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addInterceptor(new HeaderInterceptor())
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)//错误重连
                        .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                        .build();

                SERVICE = new Retrofit.Builder()
                        .baseUrl(BriefStoreConfig.URL_BASEV3)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())     //设置使用Gson解析(记得加入依赖)
                        .client(client)
                        .build().create(ApiService.class);
            }
        }
        isDefault = false;
        return SERVICE;
    }
}
