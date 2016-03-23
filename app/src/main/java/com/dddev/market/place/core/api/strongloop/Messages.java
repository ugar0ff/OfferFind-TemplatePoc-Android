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
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("status")
        private String status;
        @SerializedName("address")
        private String address;
        @SerializedName("categoryId")
        private int categoryId;
        //TODO: change type provider
        @SerializedName("provider")
        private String provider;
        @SerializedName("url")
        private String url;
        @SerializedName("price")
        private float price;
        @SerializedName("opportunityId")
        private int opportunityId;

        public ModelMessages(String jsonObject) {
            Exclude exclude = new Exclude();
            ModelMessages messages = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, ModelMessages.class);
            this.id = messages.getId();
            this.text = messages.getText();
            this.bidId = messages.getBidId();
            this.ownerId = messages.getOwnerId();
            this.senderId = messages.getSenderId();
            this.createdAt = messages.getCreatedAt();
            this.title = messages.getTitle();
            this.description = messages.getDescription();
            this.status = messages.getStatus();
            this.address = messages.getAddress();
            this.categoryId = messages.getCategoryId();
            this.provider = messages.getProvider();
            this.url = messages.getUrl();
            this.price = messages.getPrice();
            this.opportunityId = messages.getOpportunityId();
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public int getOpportunityId() {
            return opportunityId;
        }

        public void setOpportunityId(int opportunityId) {
            this.opportunityId = opportunityId;
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
            dest.writeString(this.title);
            dest.writeString(this.description);
            dest.writeString(this.status);
            dest.writeString(this.address);
            dest.writeInt(this.categoryId);
            dest.writeString(this.provider);
            dest.writeString(this.url);
            dest.writeFloat(this.price);
            dest.writeInt(this.opportunityId);
        }

        protected ModelMessages(Parcel in) {
            this.id = in.readInt();
            this.text = in.readString();
            this.ownerId = in.readInt();
            this.bidId = in.readInt();
            this.senderId = in.readInt();
            this.createdAt = in.readString();
            this.title = in.readString();
            this.description = in.readString();
            this.status = in.readString();
            this.address = in.readString();
            this.categoryId = in.readInt();
            this.provider = in.readString();
            this.url = in.readString();
            this.price = in.readFloat();
            this.opportunityId = in.readInt();
        }

        public static final Parcelable.Creator<ModelMessages> CREATOR = new Parcelable.Creator<ModelMessages>() {
            @Override
            public ModelMessages createFromParcel(Parcel source) {
                return new ModelMessages(source);
            }

            @Override
            public ModelMessages[] newArray(int size) {
                return new ModelMessages[size];
            }
        };
    }
}
