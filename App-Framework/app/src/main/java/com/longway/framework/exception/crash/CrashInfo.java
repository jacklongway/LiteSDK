package com.longway.framework.exception.crash;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;

import com.longway.framework.util.DateUtils;

/*********************************
 * Created by longway on 16/5/17 下午3:07.
 * packageName:com.longway.framework.exception.crash
 * projectName:MPTPAPP
 * Email:longway1991117@sina.com
 ********************************/
public class CrashInfo implements Parcelable {
    private String mAppName;
    private String mAppVersionCode;
    private String mAppVersionName;
    private String mSDKVersion;
    private String mDeviceName;
    private String mDeviceId;
    private String mOsVersion;
    private String mOsName;
    private String mArch;

    public String getmArch() {
        return mArch;
    }

    public void setmArch(String mArch) {
        this.mArch = mArch;
    }

    private String mProduct;
    private String mCpuCoreCount;
    private String memorySize;
    private String mErrorMessage;
    private String mThreadName;

    public String getmAppName() {
        return mAppName;
    }

    public void setmAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public String getmAppVersionCode() {
        return mAppVersionCode;
    }

    public void setmAppVersionCode(String mAppVersionCode) {
        this.mAppVersionCode = mAppVersionCode;
    }

    public String getmAppVersionName() {
        return mAppVersionName;
    }

    public void setmAppVersionName(String mAppVersionName) {
        this.mAppVersionName = mAppVersionName;
    }

    public String getmSDKVersion() {
        return mSDKVersion;
    }

    public void setmSDKVersion(String mSDKVersion) {
        this.mSDKVersion = mSDKVersion;
    }

    public String getmDeviceName() {
        return mDeviceName;
    }

    public void setmDeviceName(String mDeviceName) {
        this.mDeviceName = mDeviceName;
    }

    public String getmDeviceId() {
        return mDeviceId;
    }

    public void setmDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }

    public String getmOsVersion() {
        return mOsVersion;
    }

    public void setmOsVersion(String mOsVersion) {
        this.mOsVersion = mOsVersion;
    }

    public String getmOsName() {
        return mOsName;
    }

    public void setmOsName(String mOsName) {
        this.mOsName = mOsName;
    }

    public String getmProduct() {
        return mProduct;
    }

    public void setmProduct(String mProduct) {
        this.mProduct = mProduct;
    }


    public String getmCpuCoreCount() {
        return mCpuCoreCount;
    }

    public void setmCpuCoreCount(String mCpuCoreCount) {
        this.mCpuCoreCount = mCpuCoreCount;
    }

    public String getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(String memorySize) {
        this.memorySize = memorySize;
    }

    public String getmErrorMessage() {
        return mErrorMessage;
    }

    public void setmErrorMessage(String mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
    }

    public String getmThreadName() {
        return mThreadName;
    }

    public void setmThreadName(String mThreadName) {
        this.mThreadName = mThreadName;
    }

    public CrashInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAppName);
        dest.writeString(this.mAppVersionCode);
        dest.writeString(this.mAppVersionName);
        dest.writeString(this.mSDKVersion);
        dest.writeString(this.mDeviceName);
        dest.writeString(this.mDeviceId);
        dest.writeString(this.mOsVersion);
        dest.writeString(this.mOsName);
        dest.writeString(this.mArch);
        dest.writeString(this.mProduct);
        dest.writeString(this.mCpuCoreCount);
        dest.writeString(this.memorySize);
        dest.writeString(this.mErrorMessage);
        dest.writeString(this.mThreadName);
    }

    protected CrashInfo(Parcel in) {
        this.mAppName = in.readString();
        this.mAppVersionCode = in.readString();
        this.mAppVersionName = in.readString();
        this.mSDKVersion = in.readString();
        this.mDeviceName = in.readString();
        this.mDeviceId = in.readString();
        this.mOsVersion = in.readString();
        this.mOsName = in.readString();
        this.mArch = in.readString();
        this.mProduct = in.readString();
        this.mCpuCoreCount = in.readString();
        this.memorySize = in.readString();
        this.mErrorMessage = in.readString();
        this.mThreadName = in.readString();
    }

    @Override
    public String toString() {
        return "CrashInfo{" +
                "mAppName='" + mAppName + '\'' +
                ", mAppVersionCode='" + mAppVersionCode + '\'' +
                ", mAppVersionName='" + mAppVersionName + '\'' +
                ", mSDKVersion='" + mSDKVersion + '\'' +
                ", mDeviceName='" + mDeviceName + '\'' +
                ", mDeviceId='" + mDeviceId + '\'' +
                ", mOsVersion='" + mOsVersion + '\'' +
                ", mOsName='" + mOsName + '\'' +
                ", mArch='" + mArch + '\'' +
                ", mProduct='" + mProduct + '\'' +
                ", mCpuCoreCount='" + mCpuCoreCount + '\'' +
                ", memorySize='" + memorySize + '\'' +
                ", mErrorMessage='" + mErrorMessage + '\'' +
                ", mThreadName='" + mThreadName + '\'' +
                '}';
    }

    public void dump(Printer printer, String prefix) {
        printer.println("------crash begin------");
        printer.println("crash time:" + DateUtils.format(System.currentTimeMillis(), DateUtils.YMD_HMS));
        printer.println(prefix + "appName=" + mAppName);
        printer.println(prefix + "appVersionCode=" + mAppVersionCode);
        printer.println(prefix + "appVersionName=" + mAppVersionName);
        printer.println(prefix + "sdkVersion=" + mSDKVersion);
        printer.println(prefix + "deviceName=" + mDeviceName);
        printer.println(prefix + "deviceId=" + mDeviceId);
        printer.println(prefix + "osVersion=" + mOsVersion);
        printer.println(prefix + "osName=" + mOsName);
        printer.println(prefix + "arch=" + mArch);
        printer.println(prefix + "product=" + mProduct);
        printer.println(prefix + "cpuCoreCount=" + mCpuCoreCount);
        printer.println(prefix + "memorySize=" + memorySize);
        printer.println(prefix + "threadName=" + mThreadName);
        printer.println(prefix + "errorMessage=" + mErrorMessage);
        printer.println("------crash end------");
    }

    public static final Creator<CrashInfo> CREATOR = new Creator<CrashInfo>() {
        @Override
        public CrashInfo createFromParcel(Parcel source) {
            return new CrashInfo(source);
        }

        @Override
        public CrashInfo[] newArray(int size) {
            return new CrashInfo[size];
        }
    };
}