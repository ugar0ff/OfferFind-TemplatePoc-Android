package co.mrktplaces.clients.android.core.api.strongloop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.strongloop.android.loopback.Model;

/**
 * Created by ugar on 10.03.16.
 */
public class Account extends Model implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("bankInfo")
    private String bankInfo;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("location")
    private Location location;

    public Account(String jsonObject) {
        Exclude exclude = new Exclude();
        Account user = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, Account.class);
        this.id = user.getId();
        this.name = user.getName();
        this.location = user.getLocation();
        this.bankInfo = user.getBankInfo();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String accountId) {
        this.id = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(String bankInfo) {
        this.bankInfo = bankInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", bankInfo='" + bankInfo + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.bankInfo);
        dest.writeString(this.email);
        dest.writeString(this.avatar);
        dest.writeParcelable(this.location, flags);
    }

    protected Account(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.bankInfo = in.readString();
        this.email = in.readString();
        this.avatar = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
