package com.dddev.market.place.core.api.strongloop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.strongloop.android.loopback.Model;

import java.util.List;

/**
 * Created by ugar on 29.02.16.
 */
public class Messages extends Model {

    private List<ModelMessages> list;

    public Messages(String jsonObject) {
        Exclude exclude = new Exclude();
        this.list = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, new TypeToken<List<ModelMessages>>() {
        }.getType());
    }

    public List<ModelMessages> getList() {
        return list;
    }

    public void setList(List<ModelMessages> list) {
        this.list = list;
    }

    public static class ModelMessages implements Parcelable {
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
        @SerializedName("createdAt")
        private String createdAt;

        public ModelMessages(String jsonObject) {
            Exclude exclude = new Exclude();
            ModelMessages messages = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, ModelMessages.class);
            this.id = messages.getId();
            this.text = messages.getText();
            this.bidId = messages.getBidId();
            this.ownerId = messages.getOwnerId();
            this.senderId = messages.getSenderId();
            this.createdAt = messages.getCreatedAt();
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

        @Override
        public String toString() {
            return "ModelMessages{" +
                    "id=" + id +
                    ", text='" + text + '\'' +
                    ", ownerId=" + ownerId +
                    ", bidId=" + bidId +
                    ", senderId=" + senderId +
                    ", createdAt=" + createdAt +
                    '}';
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
            dest.writeString(this.createdAt);
        }

        protected ModelMessages(Parcel in) {
            this.id = in.readInt();
            this.text = in.readString();
            this.ownerId = in.readInt();
            this.bidId = in.readInt();
            this.senderId = in.readInt();
            this.createdAt = in.readString();
        }

        public static final Parcelable.Creator<ModelMessages> CREATOR = new Parcelable.Creator<ModelMessages>() {
            public ModelMessages createFromParcel(Parcel source) {
                return new ModelMessages(source);
            }

            public ModelMessages[] newArray(int size) {
                return new ModelMessages[size];
            }
        };
    }
}
