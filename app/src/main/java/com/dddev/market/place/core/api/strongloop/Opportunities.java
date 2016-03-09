package com.dddev.market.place.core.api.strongloop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.strongloop.android.loopback.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ugar on 19.02.16.
 */
public class Opportunities extends Model {

    private List<ModelOpportunity> list;

    public Opportunities(String jsonObject) {
        Exclude exclude = new Exclude();
        this.list = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, new TypeToken<List<ModelOpportunity>>(){}.getType());
    }

    public List<ModelOpportunity> getList() {
        return list;
    }

    public void setList(List<ModelOpportunity> list) {
        this.list = list;
    }

    public static class ModelOpportunity implements Parcelable {
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("ownerId")
        private int ownerId;
        @SerializedName("bids")
        private ArrayList<Bids.ModelBids> bids;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("categoryId")
        private int categoryId;
        @SerializedName("status")
        private int status;
        @SerializedName("Address")
        private String address;

        public ModelOpportunity() {
        }

        public ModelOpportunity(int id, String title, String description, int accountId, ArrayList<Bids.ModelBids> bids, String createAt, int categoryId, int status, String address) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.ownerId = accountId;
            this.bids = bids;
            this.createdAt = createAt;
            this.categoryId = categoryId;
            this.status = status;
            this.address = address;
        }

        public ModelOpportunity(String jsonObject) {
            Exclude exclude = new Exclude();
            ModelOpportunity opportunity = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, ModelOpportunity.class);
            this.id = opportunity.getId();
            this.title = opportunity.getTitle();
            this.description = opportunity.getDescription();
            this.ownerId = opportunity.getOwnerId();
            this.bids = opportunity.getBids();
            this.createdAt = opportunity.getCreatedAt();
            this.categoryId = opportunity.getCategoryId();
            this.status = opportunity.getStatus();
            this.address = opportunity.getAddress();
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

        public int getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(int ownerId) {
            this.ownerId = ownerId;
        }

        public ArrayList<Bids.ModelBids> getBids() {
            return bids;
        }

        public void setBids(ArrayList<Bids.ModelBids> bids) {
            this.bids = bids;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "ModelOpportunity{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", ownerId=" + ownerId +
                    ", bids=" + bids +
                    ", createdAt='" + createdAt + '\'' +
                    ", categoryId=" + categoryId +
                    ", status=" + status +
                    ", address=" + address +
                    '}';
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
            dest.writeInt(this.ownerId);
            dest.writeTypedList(bids);
            dest.writeString(this.createdAt);
            dest.writeInt(this.categoryId);
            dest.writeInt(this.status);
            dest.writeString(this.address);
        }

        protected ModelOpportunity(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
            this.description = in.readString();
            this.ownerId = in.readInt();
            this.bids = in.createTypedArrayList(Bids.ModelBids.CREATOR);
            this.createdAt = in.readString();
            this.categoryId = in.readInt();
            this.status = in.readInt();
            this.address = in.readString();
        }

        public static final Parcelable.Creator<ModelOpportunity> CREATOR = new Parcelable.Creator<ModelOpportunity>() {
            public ModelOpportunity createFromParcel(Parcel source) {
                return new ModelOpportunity(source);
            }

            public ModelOpportunity[] newArray(int size) {
                return new ModelOpportunity[size];
            }
        };
    }
}
