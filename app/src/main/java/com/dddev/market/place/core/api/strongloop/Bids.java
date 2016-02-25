package com.dddev.market.place.core.api.strongloop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.strongloop.android.loopback.Model;

import java.util.List;

/**
 * Created by ugar on 24.02.16.
 */
public class Bids extends Model {

    private List<ModelBids> list;

    public Bids(String jsonObject) {
        Exclude exclude = new Exclude();
        this.list = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, new TypeToken<List<ModelBids>>() {
        }.getType());
    }

    public List<ModelBids> getList() {
        return list;
    }

    public void setList(List<ModelBids> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Bids{" +
                "list=" + list +
                '}';
    }

    public static class ModelBids implements Parcelable {
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("url")
        private String url;
        @SerializedName("price")
        private float price;
        @SerializedName("opportunityId")
        private int opportunityId;
        private int footerHeight;

        public ModelBids() {
        }

        public ModelBids(int id, String title, String description, String url, float price, int opportunityId) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.url = url;
            this.price = price;
            this.opportunityId = opportunityId;
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

        public int getFooterHeight() {
            return footerHeight;
        }

        public void setFooterHeight(int footerHeight) {
            this.footerHeight = footerHeight;
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
            dest.writeString(this.url);
            dest.writeFloat(this.price);
            dest.writeInt(this.opportunityId);
        }

        protected ModelBids(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
            this.description = in.readString();
            this.url = in.readString();
            this.price = in.readFloat();
            this.opportunityId = in.readInt();
        }

        public static final Parcelable.Creator<ModelBids> CREATOR = new Parcelable.Creator<ModelBids>() {
            public ModelBids createFromParcel(Parcel source) {
                return new ModelBids(source);
            }

            public ModelBids[] newArray(int size) {
                return new ModelBids[size];
            }
        };

        @Override
        public String toString() {
            return "ModelBids{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", url='" + url + '\'' +
                    ", price=" + price +
                    ", opportunityId=" + opportunityId +
                    '}';
        }
    }
}
