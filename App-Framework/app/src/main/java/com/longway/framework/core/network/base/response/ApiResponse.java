package com.longway.framework.core.network.base.response;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by longway on 15/7/25.
 * 公共服务响应
 */
public class ApiResponse<T> {
    public static final int SUCCESS = 200; // 成功
    public static final int SERVER_ERROR = 500; // 错误
    public static final int NOT_FOUND = 403; // 资源找不到
    public static final int TIME_OUT = 1000; // 超时
    public static final int VALIDATE_FAIL = 1001; //认证失败
    public static final int PARSE_ERROR = 1002; // 解析出错
    private int code = SERVER_ERROR; // 状态码
    private String message;  // 状态码对应的消息
    private T obj; // 返回的对象
    private ArrayList<T> objs;
    private int currentPage; // 当前页
    private int pageSize; // 每页数据
    private int maxCount; // 总共数目
    private int maxPage; // 总共页数

    public void setObjs(ArrayList<T> objs) {
        this.objs = objs;
    }

    public ArrayList<T> getObjs() {
        return objs;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getCode() {
        return code;
    }

    public boolean isSucc() {
        return code == SUCCESS;
    }

    public String getMessage() {
        return message;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public T getObj() {
        return obj;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("****ApiResponse****");
        stringBuilder.append("code:").append(code).append(",");
        stringBuilder.append("message:").append(TextUtils.isEmpty(message) ? "unknown" : message).append(",");
        stringBuilder.append("object:").append(obj != null ? obj.toString() : "null object").append(",");
        stringBuilder.append("currentPage:").append(currentPage).append(",");
        stringBuilder.append("pageSize:").append(pageSize).append(",");
        stringBuilder.append("maxCount:").append(maxCount).append(",");
        stringBuilder.append("maxPage:").append(maxPage);
        return stringBuilder.toString();
    }
}
