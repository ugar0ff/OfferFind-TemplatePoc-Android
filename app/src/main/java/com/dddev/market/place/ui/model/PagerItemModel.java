package com.dddev.market.place.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ugar on 10.02.16.
 */
public class PagerItemModel implements Parcelable {

    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private int type;

    public PagerItemModel() {
    }

    public PagerItemModel(int id, String title, String description, String imageUrl, int type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.type = type;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.type);
    }

    private PagerItemModel(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.imageUrl = in.readString();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<PagerItemModel> CREATOR = new Parcelable.Creator<PagerItemModel>() {
        public PagerItemModel createFromParcel(Parcel source) {
            return new PagerItemModel(source);
        }

        public PagerItemModel[] newArray(int size) {
            return new PagerItemModel[size];
        }
    };
}
