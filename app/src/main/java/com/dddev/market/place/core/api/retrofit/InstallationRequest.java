package com.dddev.market.place.core.api.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ugar on 15.04.16.
 */
public class InstallationRequest {
    @SerializedName("appId")
    private String appId;
    @SerializedName("deviceToken")
    private String deviceToken;
    @SerializedName("deviceType")
    private String deviceType;
    @SerializedName("userId")
    private String userId;

    public InstallationRequest(String appId, String deviceToken, String deviceType, String userId) {
        this.appId = appId;
        this.deviceToken = deviceToken;
        this.deviceType = deviceType;
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
