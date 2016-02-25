package com.dddev.market.place.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ugar on 22.02.16.
 */
public class MessagingItemModel implements Parcelable {

    private int id;
    private String title;
    private String date;
    private String provider;
    private int state;

    public MessagingItemModel(int id, String title, String date, String provider, int state) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.provider = provider;
        this.state = state;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.date);
        dest.writeString(this.provider);
        dest.writeInt(this.state);
    }

    protected MessagingItemModel(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.date = in.readString();
        this.provider = in.readString();
        this.state = in.readInt();
    }

    public static final Parcelable.Creator<MessagingItemModel> CREATOR = new Parcelable.Creator<MessagingItemModel>() {
        public MessagingItemModel createFromParcel(Parcel source) {
            return new MessagingItemModel(source);
        }

        public MessagingItemModel[] newArray(int size) {
            return new MessagingItemModel[size];
        }
    };
}
