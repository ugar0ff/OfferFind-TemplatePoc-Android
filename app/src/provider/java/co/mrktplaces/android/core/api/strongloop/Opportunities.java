package co.mrktplaces.android.core.api.strongloop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.strongloop.android.loopback.Model;
import co.mrktplaces.android.core.api.retrofit.Account;

import java.util.List;

/**
 * Created by ugar on 02.05.16.
 */
public class Opportunities extends Model {

    private List<ModelOpportunity> list;

    public Opportunities(String jsonObject) {
        Exclude exclude = new Exclude();
        this.list = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, new TypeToken<List<ModelOpportunity>>() {
        }.getType());
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
        @SerializedName("owner")
        private Account accounts;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("categoryId")
        private int categoryId;
        @SerializedName("state")
        private String state;
        @SerializedName("location")
        private Location location;
        private String message;
        private String price;

        public ModelOpportunity() {
        }

        public ModelOpportunity(int id, String title, String description, int ownerId, Account accounts, String createAt, int categoryId, String status, Location location) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.ownerId = ownerId;
            this.accounts = accounts;
            this.createdAt = createAt;
            this.categoryId = categoryId;
            this.state = status;
            this.location = location;
        }

        public ModelOpportunity(String jsonObject) {
            Exclude exclude = new Exclude();
            ModelOpportunity opportunity = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, ModelOpportunity.class);
            this.id = opportunity.getId();
            this.title = opportunity.getTitle();
            this.description = opportunity.getDescription();
            this.ownerId = opportunity.getOwnerId();
            this.accounts = opportunity.getAccounts();
            this.createdAt = opportunity.getCreatedAt();
            this.categoryId = opportunity.getCategoryId();
            this.state = opportunity.getState();
            this.location = opportunity.getLocation();
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

        public Account getAccounts() {
            return accounts;
        }

        public void setAccounts(Account accounts) {
            this.accounts = accounts;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "ModelOpportunity{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", ownerId=" + ownerId +
                    ", accounts=" + accounts +
                    ", createdAt='" + createdAt + '\'' +
                    ", categoryId=" + categoryId +
                    ", state=" + state +
                    ", location=" + location +
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
            dest.writeParcelable(this.accounts, flags);
            dest.writeString(this.createdAt);
            dest.writeInt(this.categoryId);
            dest.writeString(this.state);
            dest.writeParcelable(this.location, flags);
            dest.writeString(this.message);
            dest.writeString(this.price);
        }

        protected ModelOpportunity(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
            this.description = in.readString();
            this.ownerId = in.readInt();
            this.accounts = in.readParcelable(Account.class.getClassLoader());
            this.createdAt = in.readString();
            this.categoryId = in.readInt();
            this.state = in.readString();
            this.location = in.readParcelable(Location.class.getClassLoader());
            this.message = in.readString();
            this.price = in.readString();
        }

        public static final Creator<ModelOpportunity> CREATOR = new Creator<ModelOpportunity>() {
            @Override
            public ModelOpportunity createFromParcel(Parcel source) {
                return new ModelOpportunity(source);
            }

            @Override
            public ModelOpportunity[] newArray(int size) {
                return new ModelOpportunity[size];
            }
        };
    }
}
