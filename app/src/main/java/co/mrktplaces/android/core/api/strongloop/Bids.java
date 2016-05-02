package co.mrktplaces.android.core.api.strongloop;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import co.mrktplaces.android.utils.PreferencesUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.strongloop.android.loopback.Model;

import java.util.ArrayList;
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
        @SerializedName("price")
        private float price;
        @SerializedName("opportunityId")
        private int opportunityId;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("ownerId")
        private int ownerId;
        @SerializedName("owner")
        private Owner owner;
        @SerializedName("state")
        private String state;
        @SerializedName("messages")
        private ArrayList<Messages> messages;

        public ModelBids() {
        }

        public ModelBids(int id, String title, String description, float price, int opportunityId, String createdAt, String state, int ownerId, Owner owner, ArrayList<Messages> messages) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.price = price;
            this.opportunityId = opportunityId;
            this.createdAt = createdAt;
            this.state = state;
            this.ownerId = ownerId;
            this.owner = owner;
            this.messages = messages;
        }

        public ModelBids(String jsonObject) {
            Exclude exclude = new Exclude();
            ModelBids bid = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, ModelBids.class);
            this.id = bid.getId();
            this.title = bid.getTitle();
            this.description = bid.getDescription();
            this.price = bid.getPrice();
            this.opportunityId = bid.getOpportunityId();
            this.createdAt = bid.getCreatedAt();
            this.owner = bid.getOwner();
            this.state = bid.getState();
            this.ownerId = bid.getOwnerId();
            this.messages = bid.getMessages();
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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Owner getOwner() {
            return owner;
        }

        public void setOwner(Owner owner) {
            this.owner = owner;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(int ownerId) {
            this.ownerId = ownerId;
        }

        public ArrayList<Messages> getMessages() {
            return messages;
        }

        public void setMessages(ArrayList<Messages> messages) {
            this.messages = messages;
        }

        public boolean isRead(Context context) {
            int userId = PreferencesUtils.getUserId(context);
            if (getMessages() != null) {
                for (Messages messages : getMessages()) {
                    if (!messages.isRead() && messages.getReceiverId() == userId) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return "ModelBids{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", price=" + price +
                    ", opportunityId=" + opportunityId +
                    ", date='" + createdAt + '\'' +
                    ", ownerId='" + ownerId + '\'' +
                    ", owner='" + owner + '\'' +
                    ", messages='" + messages + '\'' +
                    ", state=" + state +
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
            dest.writeFloat(this.price);
            dest.writeInt(this.opportunityId);
            dest.writeString(this.createdAt);
            dest.writeInt(this.ownerId);
            dest.writeParcelable(this.owner, flags);
            dest.writeString(this.state);
            dest.writeTypedList(messages);
        }

        protected ModelBids(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
            this.description = in.readString();
            this.price = in.readFloat();
            this.opportunityId = in.readInt();
            this.createdAt = in.readString();
            this.ownerId = in.readInt();
            this.owner = in.readParcelable(Owner.class.getClassLoader());
            this.state = in.readString();
            this.messages = in.createTypedArrayList(Messages.CREATOR);
        }

        public static final Parcelable.Creator<ModelBids> CREATOR = new Parcelable.Creator<ModelBids>() {
            @Override
            public ModelBids createFromParcel(Parcel source) {
                return new ModelBids(source);
            }

            @Override
            public ModelBids[] newArray(int size) {
                return new ModelBids[size];
            }
        };
    }
}
