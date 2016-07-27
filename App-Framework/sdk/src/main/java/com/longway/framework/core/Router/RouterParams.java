package com.longway.framework.core.Router;

import android.net.Uri;
import android.os.Bundle;

import java.util.List;

/**
 * Created by longway on 16/7/21.
 * Email:longway1991117@sina.com
 */

public class RouterParams {
    public String mClassName;
    public int mFlag = -1;
    public Class<?> mClz;
    public String mAction;
    public List<String> mCategory;
    public String mPermission;
    public Uri mData;
    public String mType;
    public Bundle mParams;
    public String mKey;
    public int mRequestCode;

    public int getRequestCode() {
        return mRequestCode;
    }

    public void setRequestCode(int mRequestCode) {
        this.mRequestCode = mRequestCode;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(String mClassName) {
        this.mClassName = mClassName;
    }

    public int getFlag() {
        return mFlag;
    }

    public void setFlag(int mFlag) {
        this.mFlag = mFlag;
    }

    public Class<?> getClz() {
        return mClz;
    }

    public void setClz(Class<?> mClz) {
        this.mClz = mClz;
    }

    public String getAction() {
        return mAction;
    }

    public void setAction(String mAction) {
        this.mAction = mAction;
    }

    public List<String> getCategory() {
        return mCategory;
    }

    public void setCategory(List<String> mCategory) {
        this.mCategory = mCategory;
    }

    public String getPermission() {
        return mPermission;
    }

    public void setPermission(String mPermission) {
        this.mPermission = mPermission;
    }

    public Uri getData() {
        return mData;
    }

    public void setData(Uri mData) {
        this.mData = mData;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public Bundle getParams() {
        return mParams;
    }

    public void setParams(Bundle mParams) {
        this.mParams = mParams;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RouterParams{");
        sb.append("mClassName='").append(mClassName).append('\'');
        sb.append(", mFlag=").append(mFlag);
        sb.append(", mClz=").append(mClz);
        sb.append(", mAction='").append(mAction).append('\'');
        sb.append(", mCategory=").append(mCategory);
        sb.append(", mPermission='").append(mPermission).append('\'');
        sb.append(", mData=").append(mData);
        sb.append(", mType='").append(mType).append('\'');
        sb.append(", mParams=").append(mParams);
        sb.append(", mKey='").append(mKey).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String toString(String prefix) {
        final StringBuffer sb = new StringBuffer();
        if (prefix != null) {
            sb.append(prefix).append("-");
        }
        sb.append("RouterParams{");
        sb.append("mClassName='").append(mClassName).append('\'');
        sb.append(", mFlag=").append(mFlag);
        sb.append(", mClz=").append(mClz);
        sb.append(", mAction='").append(mAction).append('\'');
        sb.append(", mCategory=").append(mCategory);
        sb.append(", mPermission='").append(mPermission).append('\'');
        sb.append(", mData=").append(mData);
        sb.append(", mType='").append(mType).append('\'');
        sb.append(", mParams=").append(mParams);
        sb.append(", mKey='").append(mKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
