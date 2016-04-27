package co.mrktplaces.clients.android.ui.model;

import co.mrktplaces.clients.android.core.api.strongloop.Exclude;
import co.mrktplaces.clients.android.core.api.strongloop.StreamModel;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ugar on 04.03.16.
 */
public class StreamMessageDataModel {

    @SerializedName("target")
    public int target;
    @SerializedName("data")
    public StreamModel.ModelMessages data;
    @SerializedName("type")
    public String type;
    @SerializedName("class")
    public String className;

    public StreamMessageDataModel(String jsonObject) {
        Exclude exclude = new Exclude();
        StreamMessageDataModel messageDataModel = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, StreamMessageDataModel.class);
        this.target = messageDataModel.getTarget();
        this.data = messageDataModel.getData();
        this.type = messageDataModel.getType();
        this.className = messageDataModel.getClassName();
    }

    public StreamMessageDataModel(int target, StreamModel.ModelMessages data, String type, String className) {
        this.target = target;
        this.data = data;
        this.type = type;
        this.className = className;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public StreamModel.ModelMessages getData() {
        return data;
    }

    public void setData(StreamModel.ModelMessages data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
