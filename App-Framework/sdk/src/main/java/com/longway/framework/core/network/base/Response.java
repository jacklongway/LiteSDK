package com.longway.framework.core.network.base;

import java.util.HashMap;
import java.util.List;

/*********************************
 * Created by longway on 16/5/8 下午6:23.
 * packageName:com.longway.framework.core.network.network.response
 * projectName:MainProject
 * Email:longway1991117@sina.com
 ********************************/
public class Response {
    private int code;
    private String message;
    private String protocol;
    private HashMap<String, List<String>> header;
    private byte[] sourceBody;
    private String stringBody;
    private Request request;
    private Object mBusinessResponse;

    public void setBussinessResponse(Object businessResponse) {
        this.mBusinessResponse = businessResponse;
    }

    public Object getBusinessResponse() {
        return mBusinessResponse;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public byte[] getSourceBody() {
        return sourceBody;
    }

    public void setSourceBody(byte[] sourceBody) {
        this.sourceBody = sourceBody;
    }

    public String getBody() {
        return stringBody;
    }

    public void setBody(String body) {
        this.stringBody = body;
    }

    public Response() {
        header = new HashMap<>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void addHeader(String key, List<String> value) {
        header.put(key, value);
    }

    public List<String> getValue(String key) {
        return header.get(key);
    }
}
