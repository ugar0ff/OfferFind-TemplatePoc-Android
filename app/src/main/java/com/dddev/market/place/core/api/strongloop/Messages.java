package com.dddev.market.place.core.api.strongloop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ugar on 07.04.16.
 */
public class Messages implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("text")
    private String text;
    @SerializedName("ownerId")
    private int ownerId;
    @SerializedName("bidId")
    private int bidId;
    @SerializedName("senderId")
    private int senderId;
    @SerializedName("receiverId")
    private int receiverId;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("read")
    private boolean read;

    public Messages() {

    }

    public Messages(int id, String text, int ownerId, int bidId, int senderId, int receiverId, String createdAt, boolean read) {
        this.id = id;
        this.text = text;
        this.ownerId = ownerId;
        this.bidId = bidId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.createdAt = createdAt;
        this.read = read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.text);
        dest.writeInt(this.ownerId);
        dest.writeInt(this.bidId);
        dest.writeInt(this.senderId);
        dest.writeInt(this.receiverId);
        dest.writeString(this.createdAt);
        dest.writeByte(read ? (byte) 1 : (byte) 0);
    }

    protected Messages(Parcel in) {
        this.id = in.readInt();
        this.text = in.readString();
        this.ownerId = in.readInt();
        this.bidId = in.readInt();
        this.senderId = in.readInt();
        this.receiverId = in.readInt();
        this.createdAt = in.readString();
        this.read = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Messages> CREATOR = new Parcelable.Creator<Messages>() {
        @Override
        public Messages createFromParcel(Parcel source) {
            return new Messages(source);
        }

        @Override
        public Messages[] newArray(int size) {
            return new Messages[size];
        }
    };

    @Override
    public String toString() {
        return "Messages{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", ownerId=" + ownerId +
                ", bidId=" + bidId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", createdAt='" + createdAt + '\'' +
                ", read=" + read +
                '}';
    }
}
