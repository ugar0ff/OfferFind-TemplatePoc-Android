package com.dddev.market.place.core.api.strongloop;

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
    @SerializedName("address")
    private String address;
    @SerializedName("bankInfo")
    private String bankInfo;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private String avatar;

    public Account(String jsonObject) {
        Exclude exclude = new Exclude();
        Account user = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, Account.class);
        this.id = user.getId();
        this.name = user.getName();
        this.address = user.getAddress();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
                ", address='" + address + '\'' +
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
        dest.writeString(this.address);
        dest.writeString(this.bankInfo);
        dest.writeString(this.email);
        dest.writeString(this.avatar);
    }

    protected Account(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.bankInfo = in.readString();
        this.email = in.readString();
        this.avatar = in.readString();
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
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
