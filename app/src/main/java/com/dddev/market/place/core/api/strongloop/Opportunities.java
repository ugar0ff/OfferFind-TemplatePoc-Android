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
        @SerializedName("accountId")
        private int accountId;
        @SerializedName("bids")
        private ArrayList<Bids.ModelBids> bids;
        @SerializedName("createAt")
        private long createAt;
        @SerializedName("categoryId")
        private int categoryId;

        public ModelOpportunity() {
        }

        public ModelOpportunity(int id, String title, String description, int accountId, ArrayList<Bids.ModelBids> bids, long createAt, int categoryId) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.accountId = accountId;
            this.bids = bids;
            this.createAt = createAt;
            this.categoryId = categoryId;
        }

        public ModelOpportunity(String jsonObject) {
            Exclude exclude = new Exclude();
            ModelOpportunity opportunity = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, ModelOpportunity.class);
            this.id = opportunity.getId();
            this.title = opportunity.getTitle();
            this.description = opportunity.getDescription();
            this.accountId = opportunity.getAccountId();
            this.bids = opportunity.getBids();
            this.createAt = opportunity.getCreateAt();
            this.categoryId = opportunity.getCategoryId();
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

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public ArrayList<Bids.ModelBids> getBids() {
            return bids;
        }

        public void setBids(ArrayList<Bids.ModelBids> bids) {
            this.bids = bids;
        }

        public long getCreateAt() {
            return createAt;
        }

        public void setCreateAt(long createAt) {
            this.createAt = createAt;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        @Override
        public String toString() {
            return "ModelOpportunity{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", accountId=" + accountId +
                    ", bids=" + bids +
                    ", createAt='" + createAt + '\'' +
                    ", categoryId=" + categoryId +
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
            dest.writeInt(this.accountId);
            dest.writeTypedList(bids);
            dest.writeLong(this.createAt);
            dest.writeInt(this.categoryId);
        }

        protected ModelOpportunity(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
            this.description = in.readString();
            this.accountId = in.readInt();
            this.bids = in.createTypedArrayList(Bids.ModelBids.CREATOR);
            this.createAt = in.readLong();
            this.categoryId = in.readInt();
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
