package com.longway.framework.bean;

import android.os.Parcel;

import com.longway.framework.util.JsonUtil;

/**
 * Created by longway on 15/7/25.
 * 基本业务对象
 */
public class BaseBean {

    public void writeToParcel(Parcel dest, int flags) {
    }

    public void readFromParcel(Parcel src) {

    }

    public BaseBean() {
    }

    public String json() {
        return JsonUtil.toJson(this);
    }
}
