package co.mrktplaces.android.core.api.strongloop;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.strongloop.android.loopback.Model;

/**
 * Created by ugar on 23.02.16.
 */
public class FindOne  extends Model {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("owner")
    private String owner;
    @SerializedName("clientKey")
    private String clientKey;
    @SerializedName("javaScriptKey")
    private String javaScriptKey;
    @SerializedName("restApiKey")
    private String restApiKey;
    @SerializedName("windowsKey")
    private String windowsKey;
    @SerializedName("masterKey")
    private String masterKey;
    @SerializedName("pushSettings")
    private PushSettings pushSettings;
    @SerializedName("authenticationEnabled")
    private boolean authenticationEnabled;
    @SerializedName("anonymousAllowed")
    private boolean anonymousAllowed;
    @SerializedName("status")
    private String status;
    @SerializedName("created")
    private String created;
    @SerializedName("modified")
    private String modified;

    public FindOne(String jsonObject) {
        Exclude exclude = new Exclude();
        FindOne findOne = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, FindOne.class);
        this.id = findOne.getId();
        this.name = findOne.getName();
        this.owner = findOne.getOwner();
        this.clientKey = findOne.getClientKey();
        this.javaScriptKey = findOne.getJavaScriptKey();
        this.restApiKey = findOne.getRestApiKey();
        this.windowsKey = findOne.getWindowsKey();
        this.masterKey = findOne.getMasterKey();
        this.pushSettings = findOne.getPushSettings();
        this.authenticationEnabled = findOne.isAuthenticationEnabled();
        this.anonymousAllowed = findOne.isAnonymousAllowed();
        this.status = findOne.getStatus();
        this.created = findOne.getCreated();
        this.modified = findOne.getModified();
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getJavaScriptKey() {
        return javaScriptKey;
    }

    public void setJavaScriptKey(String javaScriptKey) {
        this.javaScriptKey = javaScriptKey;
    }

    public String getRestApiKey() {
        return restApiKey;
    }

    public void setRestApiKey(String restApiKey) {
        this.restApiKey = restApiKey;
    }

    public String getWindowsKey() {
        return windowsKey;
    }

    public void setWindowsKey(String windowsKey) {
        this.windowsKey = windowsKey;
    }

    public String getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(String masterKey) {
        this.masterKey = masterKey;
    }

    public PushSettings getPushSettings() {
        return pushSettings;
    }

    public void setPushSettings(PushSettings pushSettings) {
        this.pushSettings = pushSettings;
    }

    public boolean isAuthenticationEnabled() {
        return authenticationEnabled;
    }

    public void setAuthenticationEnabled(boolean authenticationEnabled) {
        this.authenticationEnabled = authenticationEnabled;
    }

    public boolean isAnonymousAllowed() {
        return anonymousAllowed;
    }

    public void setAnonymousAllowed(boolean anonymousAllowed) {
        this.anonymousAllowed = anonymousAllowed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return "FindOne{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", clientKey='" + clientKey + '\'' +
                ", javaScriptKey='" + javaScriptKey + '\'' +
                ", restApiKey='" + restApiKey + '\'' +
                ", windowsKey='" + windowsKey + '\'' +
                ", masterKey='" + masterKey + '\'' +
                ", pushSettings=" + pushSettings.toString() +
                ", authenticationEnabled=" + authenticationEnabled +
                ", anonymousAllowed=" + anonymousAllowed +
                ", status='" + status + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                '}';
    }

    public class PushSettings {
        @SerializedName("gcm")
        private Gcm gcm;

        public PushSettings(Gcm gcm) {
            this.gcm = gcm;
        }

        public Gcm getGcm() {
            return gcm;
        }

        public void setGcm(Gcm gcm) {
            this.gcm = gcm;
        }

        @Override
        public String toString() {
            return "PushSettings{" +
                    "gcm=" + gcm.toString() +
                    '}';
        }

        public class Gcm {
            @SerializedName("serverApiKey")
            private String serverApiKey;

            public Gcm(String serverApiKey) {
                this.serverApiKey = serverApiKey;
            }
            public String getServerApiKey() {
                return serverApiKey;
            }

            public void setServerApiKey(String serverApiKey) {
                this.serverApiKey = serverApiKey;
            }

            @Override
            public String toString() {
                return "Gcm{" +
                        "serverApiKey='" + serverApiKey + '\'' +
                        '}';
            }

        }
    }
}
