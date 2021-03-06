package co.mrktplaces.android.core.api.strongloop;

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
public class StreamModel extends Model {

    private List<ModelMessages> list;

    public StreamModel(String jsonObject) {
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
        @SerializedName("receiverId")
        private int receiverId;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("read")
        private boolean read;
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("state")
        private String state;
        @SerializedName("categoryId")
        private int categoryId;
        @SerializedName("owner")
        private Owner owner;
        @SerializedName("price")
        private float price;
        @SerializedName("opportunityId")
        private int opportunityId;
        @SerializedName("location")
        private Location location;

        public ModelMessages(String jsonObject) {
            Exclude exclude = new Exclude();
            ModelMessages messages = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, ModelMessages.class);
            this.id = messages.getId();
            this.text = messages.getText();
            this.bidId = messages.getBidId();
            this.ownerId = messages.getOwnerId();
            this.senderId = messages.getSenderId();
            this.receiverId = messages.getReceiverId();
            this.createdAt = messages.getCreatedAt();
            this.title = messages.getTitle();
            this.description = messages.getDescription();
            this.state = messages.getState();
            this.categoryId = messages.getCategoryId();
            this.owner = messages.getOwner();
            this.price = messages.getPrice();
            this.opportunityId = messages.getOpportunityId();
            this.read = messages.isRead();
            this.location = messages.getLocation();
        }

        public ModelMessages() {
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public Owner getOwner() {
            return owner;
        }

        public void setOwner(Owner owner) {
            this.owner = owner;
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

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
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
            dest.writeString(this.title);
            dest.writeString(this.description);
            dest.writeString(this.state);
            dest.writeInt(this.categoryId);
            dest.writeParcelable(this.owner, flags);
            dest.writeFloat(this.price);
            dest.writeInt(this.opportunityId);
            dest.writeParcelable(this.location, flags);
        }

        protected ModelMessages(Parcel in) {
            this.id = in.readInt();
            this.text = in.readString();
            this.ownerId = in.readInt();
            this.bidId = in.readInt();
            this.senderId = in.readInt();
            this.receiverId = in.readInt();
            this.createdAt = in.readString();
            this.read = in.readByte() != 0;
            this.title = in.readString();
            this.description = in.readString();
            this.state = in.readString();
            this.categoryId = in.readInt();
            this.owner = in.readParcelable(Owner.class.getClassLoader());
            this.price = in.readFloat();
            this.opportunityId = in.readInt();
            this.location = in.readParcelable(Location.class.getClassLoader());
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
