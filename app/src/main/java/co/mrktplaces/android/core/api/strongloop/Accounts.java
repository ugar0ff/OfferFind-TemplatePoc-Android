package co.mrktplaces.android.core.api.strongloop;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.strongloop.android.loopback.Model;

/**
 * Created by ugar on 16.02.16.
 */
public class Accounts extends Model {
    @SerializedName("id")
    private String id;
    @SerializedName("ttl")
    private long ttl;
    @SerializedName("created")
    private String created;
    @SerializedName("userId")
    private int userId;

    public Accounts(String jsonObject) {
        Exclude exclude = new Exclude();
        Accounts user = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, Accounts.class);
        this.id = user.getId();
        this.ttl = user.getTtl();
        this.created = user.getCreated();
        this.userId = user.getUserId();
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "id='" + id + '\'' +
                ", ttl=" + ttl +
                ", created='" + created + '\'' +
                ", userId=" + userId +
                '}';
    }
}

