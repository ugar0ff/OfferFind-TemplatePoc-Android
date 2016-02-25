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
        @SerializedName("date")
        private long date;
        @SerializedName("status")
        private int status;

        public ModelOpportunity() {
        }

        public ModelOpportunity(int id, String title, String description, int accountId, ArrayList<Bids.ModelBids> bids, long date, int status) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.accountId = accountId;
            this.bids = bids;
            this.date = date;
            this.status = status;
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

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "ModelOpportunity{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", accountId=" + accountId +
                    ", bids=" + bids +
                    ", date='" + date + '\'' +
                    ", status=" + status +
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
            dest.writeLong(this.date);
            dest.writeInt(this.status);
        }

        protected ModelOpportunity(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
            this.description = in.readString();
            this.accountId = in.readInt();
            this.bids = in.createTypedArrayList(Bids.ModelBids.CREATOR);
            this.date = in.readLong();
            this.status = in.readInt();
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
