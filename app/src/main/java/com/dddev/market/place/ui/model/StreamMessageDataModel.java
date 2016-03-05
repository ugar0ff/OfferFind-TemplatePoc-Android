package com.dddev.market.place.ui.model;

import com.dddev.market.place.core.api.strongloop.Exclude;
import com.dddev.market.place.core.api.strongloop.Messages;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ugar on 04.03.16.
 */
public class StreamMessageDataModel {

    @SerializedName("target")
    public int target;
    @SerializedName("data")
    public Messages.ModelMessages data;
    @SerializedName("type")
    public String type;

    public StreamMessageDataModel(String jsonObject) {
        Exclude exclude = new Exclude();
        StreamMessageDataModel messageDataModel = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, StreamMessageDataModel.class);
        this.target = messageDataModel.getTarget();
        this.data = messageDataModel.getData();
        this.type = messageDataModel.getType();
    }

    public StreamMessageDataModel(int target, Messages.ModelMessages data, String type) {
        this.target = target;
        this.data = data;
        this.type = type;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public Messages.ModelMessages getData() {
        return data;
    }

    public void setData(Messages.ModelMessages data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
