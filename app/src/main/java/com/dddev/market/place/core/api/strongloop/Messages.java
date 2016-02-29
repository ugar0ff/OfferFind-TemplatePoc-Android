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
        @SerializedName("senderId")
        private int senderId;
        @SerializedName("bidId")
        private int bidId;
        @SerializedName("receiverId")
        private int receiverId;

        public ModelMessages(String jsonObject) {
            Exclude exclude = new Exclude();
            ModelMessages messages = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, ModelMessages.class);
            this.id = messages.getId();
            this.text = messages.getText();
            this.senderId = messages.getSenderId();
            this.receiverId = messages.getSenderId();
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

        public int getSenderId() {
            return senderId;
        }

        public void setSenderId(int senderId) {
            this.senderId = senderId;
        }

        public int getBidId() {
            return bidId;
        }

        public void setBidId(int bidId) {
            this.bidId = bidId;
        }

        public int getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(int receiverId) {
            this.receiverId = receiverId;
        }

        @Override
        public String toString() {
            return "ModelMessages{" +
                    "id=" + id +
                    ", text='" + text + '\'' +
                    ", senderId=" + senderId +
                    ", bidId=" + bidId +
                    ", receiverId=" + receiverId +
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
            dest.writeInt(this.senderId);
            dest.writeInt(this.bidId);
            dest.writeInt(this.receiverId);
        }

        protected ModelMessages(Parcel in) {
            this.id = in.readInt();
            this.text = in.readString();
            this.senderId = in.readInt();
            this.bidId = in.readInt();
            this.receiverId = in.readInt();
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
