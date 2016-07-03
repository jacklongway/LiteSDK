package com.longway.framework.report;

/**
 * 必须要字段：IMEI，IMSI，MAC，ICCID，Android Id，API Level，Model，Brand
 * 在考虑是否需要加上的字段：Rom版本，Linux版本，UserAgent
 */
public final class PhoneInfo {

    private String mImei = "";

    private String mImsi = "";

    private String mMac = "";

    private String mIccid = "";

    private String mAndroidId = "";

    /**
     * 初始化为-1
     */
    private String mApiLevel = "";

    private String mModel = "";

    private String mBrand = "";

    private int mNetworkState;

    private String mProductId = "";

    private String mAppVersion = "";

    private String mGuid = "";


    public int getNetworkState() {
        return mNetworkState;
    }

    public void setNetworkState(int networkState) {
        this.mNetworkState = networkState;
    }

    public String getProductID() {
        return mProductId;
    }

    public void setProductID(String productID) {
        this.mProductId = productID;
    }

    public String getAppVersion() {
        return mAppVersion;
    }

    public void setAppVersion(String appVersion) {
        this.mAppVersion = appVersion;
    }

    public String className() {
        return "";
    }

    public String fullClassName() {
        return "";
    }

    public void setImei(String imei) {
        this.mImei = imei;
    }

    public void setImsi(String imsi) {
        this.mImsi = imsi;
    }

    public void setMac(String mac) {
        this.mMac = mac;
    }

    public void setIccid(String iccid) {
        this.mIccid = iccid;
    }

    public void setAndroidId(String androidId) {
        this.mAndroidId = androidId;
    }

    public void setApiLevel(String apiLevel) {
        this.mApiLevel = apiLevel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }

    public void setBrand(String brand) {
        this.mBrand = brand;
    }

    public void setGuid(String guid) {
        this.mGuid = guid;
    }

    public String getImei() {
        return mImei;
    }

    public String getImsi() {
        return mImsi;
    }

    public String getMac() {
        return mMac;
    }

    public String getIccid() {
        return mIccid;
    }

    public String getAndroidId() {
        return mAndroidId;
    }

    public String getApiLevel() {
        return mApiLevel;
    }

    public String getModel() {
        return mModel;
    }

    public String getBrand() {
        return mBrand;
    }

    public String getGuid() {
        return mGuid;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("PhoneInfo{\n")
                .append("imei=").append(mImei).append("\n")
                .append("imsi=").append(mImsi).append("\n")
                .append("mac=").append(mMac).append("\n")
                .append("iccid=").append(mIccid).append("\n")
                .append("androidId=").append(mAndroidId).append("\n")
                .append("apiLevel=").append(mApiLevel).append("\n")
                .append("model=").append(mModel).append("\n")
                .append("brand=").append(mBrand).append("\n")
                .append("networkState=").append(mNetworkState).append("\n")
                .append("productId=").append(mProductId).append("\n")
                .append("appVersion=").append(mAppVersion).append("\n")
                .append("}");

        return sb.toString();
    }
}