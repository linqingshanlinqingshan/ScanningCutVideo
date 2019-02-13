package com.example.administrator.retrofitmvp;

public class HttpHeader {

    private String headerKey;
    private String headerValue;

    public HttpHeader() {

    }

    public HttpHeader(String headerKey, String headerValue) {
        this.headerKey = headerKey;
        this.headerValue = headerValue;
    }

    public HttpHeader setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
        return this;
    }

    public HttpHeader setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
        return this;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public String getHeaderValue() {
        return headerValue;
    }
}
