package com.dddev.market.place.core.api.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ugar on 23.02.16.
 */
public class Installation {
    @SerializedName("appId")
    private String appId;
    @SerializedName("created")
    private String created;
    @SerializedName("deviceToken")
    private String deviceToken;
    @SerializedName("deviceType")
    private String deviceType;
    @SerializedName("modified")
    private String modified;
    @SerializedName("userId")
    private String userId;
    @SerializedName("id")
    private int id;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getIdInstall() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Installation{" +
                "appId='" + appId + '\'' +
                ", created='" + created + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", modified='" + modified + '\'' +
                ", userId='" + userId + '\'' +
                ", id=" + id +
                '}';
    }
}
