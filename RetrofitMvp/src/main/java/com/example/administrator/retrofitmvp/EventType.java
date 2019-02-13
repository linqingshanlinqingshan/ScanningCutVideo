package com.example.administrator.retrofitmvp;

import java.util.Map;

public class EventType {

    private String mMsg;
    private int EventTypeCode;
    Map<Object, Object> params;

    public static final int code_refresh_productconnection = 1;                      // 关闭页面
    public static final int code_refresh_caseconnection = 2;                         // 关闭页面
    public static final int code_normal_intoshoprecord = 3;                          // 到店记录
    public static final int code_refreshMeinfo = 4;                                  // 刷新个人信息
    public static final int code_refreshMessage = 5;                                  // 刷新个人信息

    // 推送
    public static final String PARAM_ISJPUSH = "param_isjpush";             // 是否是极光推送
    public static final String PARAM_JPUSHBEAN = "param_jpushBean";         // 极光推送实体

    public EventType(int EventTypeCode) {
        this.EventTypeCode = EventTypeCode;
    }

    public EventType(int EventTypeCode, Map<Object, Object> params) {
        this.EventTypeCode = EventTypeCode;
        this.params = params;
    }

    public EventType(String msg) {
        mMsg = msg;
    }

    public int getEventTypeCode() {
        return EventTypeCode;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setParams(Map<Object, Object> params) {
        this.params = params;
    }

    public Map<Object, Object> getParams() {
        return params;
    }


}