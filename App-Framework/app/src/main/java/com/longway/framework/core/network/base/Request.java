package com.longway.framework.core.network.base;

import android.text.TextUtils;
import android.util.Printer;

import com.longway.framework.util.DateUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by longway on 16/4/26.
 * Email:longway1991117@sina.com
 */
public class Request {
    private static final String TAG = Request.class.getSimpleName();
    private static final String DEFAULT_PREFIX = TAG;
    private String mURL;
    private HashMap<String, String> mURLParams;
    private HashMap<String, String> mBodyParams;
    private HashMap<String, String> mHeader;
    private HashMap<String, File> mFileBody;
    private Collection<Object> mArray;
    private String mTag;
    private int mExtra;
    private String mLocalRepositoryPath;
    private CacheControl mCacheControl;
    private Type mBusinessType;

    public void setBusinessType(Type businessType) {
        this.mBusinessType = businessType;
    }

    public Type getBusinessType() {
        return mBusinessType;
    }

    public void setLocalRepositoryPath(String localRepositoryPath) {
        this.mLocalRepositoryPath = localRepositoryPath;
    }

    public String getLocalRepositoryPath() {
        return mLocalRepositoryPath;
    }

    public void addFileBody(String key, File file) {
        mFileBody.put(key, file);
    }

    public void removeFileBody(String key) {
        mFileBody.remove(key);
    }

    public HashMap<String, File> getFileBody() {
        return mFileBody;
    }

    public CacheControl getCacheControl() {
        return mCacheControl;
    }

    public int getExtra() {
        return mExtra;
    }

    public void setExtra(int extra) {
        this.mExtra = extra;
    }

    public String getRealTag() {
        String tag = getTag();
        if (TextUtils.isEmpty(tag)) {
            tag = mURL;
        }
        return tag;
    }

    public void setCacheControl(CacheControl cacheControl) {
        this.mCacheControl = cacheControl;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public Request(String url) {
        this.mURL = url;
        this.mURLParams = new HashMap<>();
        this.mBodyParams = new HashMap<>();
        this.mFileBody = new HashMap<>();
        this.mHeader = new HashMap<>();
        this.mArray = new ArrayList<>();
    }

    public void addBodyParams(String key, String value) {
        mBodyParams.put(key, value);
    }

    public void removeBodyParams(String key) {
        mBodyParams.remove(key);
    }

    public HashMap<String, String> getBodyParams() {
        return mBodyParams;
    }

    public void addObject(Object o) {
        mArray.add(o);
    }

    public Collection<Object> getArray() {
        return mArray;
    }

    public String addURLParams(String key, String value) {
        return mURLParams.put(key, value);
    }

    public String removeURLParams(String key) {
        return mURLParams.remove(key);
    }

    public String addHeader(String key, String value) {
        return mHeader.put(key, value);
    }

    public String removeHeader(String key) {
        return mHeader.remove(key);
    }

    public String getURL() {
        return mURL;
    }

    public HashMap<String, String> getURLParams() {
        return mURLParams;
    }

    public HashMap<String, String> getHeader() {
        return mHeader;
    }

    public void dump(Printer printer, String prefix) {
        printer.println("-----------dump start-----------");
        printer.println("occur time:" + DateUtils.format(System.currentTimeMillis(), DateUtils.YMD_HMS));
        printer.println(prefix != null ? prefix : DEFAULT_PREFIX + "<< url:" + (mURL != null ? mURL : "mURL null"));
        HashMap<String, String> params = mURLParams;
        printer.println("***params header***");
        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                printer.println(prefix != null ? prefix : DEFAULT_PREFIX + "<<" + key + ":" + params.get(key));
            }
        } else {
            printer.println("no params");
        }
        printer.println("***params tail***");
        printer.println("-----------------");
        printer.println("***header head***");
        HashMap<String, String> header = mHeader;
        if (header != null && !header.isEmpty()) {
            Set<String> keys = header.keySet();
            for (String key : keys) {
                printer.println(prefix != null ? prefix : DEFAULT_PREFIX + "<<" + key + ":" + header.get(key));
            }
        } else {
            printer.println("no header");
        }
        printer.println("***header tail***");
        printer.println("-----------dump end-------------");
    }

}
