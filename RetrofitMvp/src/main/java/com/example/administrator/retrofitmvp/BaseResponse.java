package com.example.administrator.retrofitmvp;


import org.litepal.crud.DataSupport;


public class BaseResponse extends DataSupport {

    private String msg;
    private int status;

    public boolean success() {
        return status == Api.CODE_SUCESS;
    }

    public String getMessage() {
        return msg == null ? "" : msg;
    }

    public BaseResponse setMessage(String msg) {
        this.msg = msg;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public BaseResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                '}';
    }


}
