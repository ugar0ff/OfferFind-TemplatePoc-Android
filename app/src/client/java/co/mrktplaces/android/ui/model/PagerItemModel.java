package co.mrktplaces.android.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ugar on 10.02.16.
 */
public class PagerItemModel implements Parcelable {

    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private int type;
    private double latitude;
    private double longitude;
    private  String address;

    public PagerItemModel(int id, String title, String description, String imageUrl, int type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.type = type;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        dest.writeString(this.imageUrl);
        dest.writeInt(this.type);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.address);
    }

    protected PagerItemModel(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.imageUrl = in.readString();
        this.type = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.address = in.readString();
    }

    public static final Creator<PagerItemModel> CREATOR = new Creator<PagerItemModel>() {
        @Override
        public PagerItemModel createFromParcel(Parcel source) {
            return new PagerItemModel(source);
        }

        @Override
        public PagerItemModel[] newArray(int size) {
            return new PagerItemModel[size];
        }
    };
}
