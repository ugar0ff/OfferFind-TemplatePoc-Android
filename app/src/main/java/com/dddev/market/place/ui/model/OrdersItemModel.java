package com.dddev.market.place.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ugar on 22.02.16.
 */
public class OrdersItemModel implements Parcelable {

    private int id;
    private String title;
    private String date;
    private int state;

    public OrdersItemModel(int id, String title, String date, int state) {
        this.id = id;
        this.title = title;
        this.date = date;
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
        dest.writeInt(this.state);
    }

    protected OrdersItemModel(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.date = in.readString();
        this.state = in.readInt();
    }

    public static final Parcelable.Creator<OrdersItemModel> CREATOR = new Parcelable.Creator<OrdersItemModel>() {
        public OrdersItemModel createFromParcel(Parcel source) {
            return new OrdersItemModel(source);
        }

        public OrdersItemModel[] newArray(int size) {
            return new OrdersItemModel[size];
        }
    };
}
