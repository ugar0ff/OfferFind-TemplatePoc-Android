package com.dddev.market.place.core.api.strongloop;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.strongloop.android.loopback.Model;

/**
 * Created by ugar on 10.03.16.
 */
public class Account extends Model {

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

    public Account(String jsonObject) {
        Exclude exclude = new Exclude();
        Account user = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, Account.class);
        this.id = user.getId();
        this.name = user.getName();
        this.address = user.getAddress();
        this.bankInfo = user.getBankInfo();
        this.email = user.getEmail();
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", bankInfo='" + bankInfo + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
