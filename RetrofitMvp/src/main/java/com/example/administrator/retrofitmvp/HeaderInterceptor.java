package com.example.administrator.retrofitmvp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class HeaderInterceptor implements Interceptor {

    private List<HttpHeader> httpHeaders = new ArrayList<HttpHeader>();

    public HeaderInterceptor() {}

    public HeaderInterceptor(HttpHeader... params) {
        if (params!=null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                httpHeaders.add((HttpHeader) params[i]);
            }
        }
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder()
//                .addHeader("Content-Type", "application/json;charset=utf8")
                .addHeader("Content-Type", "multipart/form-data")
                .addHeader("Accept", "application/json")
                .method(originalRequest.method(), originalRequest.body());

        if (httpHeaders!=null && httpHeaders.size() > 0) {
            for (int i = 0; i < httpHeaders.size(); i++) {
                requestBuilder.addHeader(httpHeaders.get(i).getHeaderKey(), httpHeaders.get(i).getHeaderValue());
            }
        }
        Request request = requestBuilder.build();
        Response response = chain.proceed(request);
        return response;

    }

}