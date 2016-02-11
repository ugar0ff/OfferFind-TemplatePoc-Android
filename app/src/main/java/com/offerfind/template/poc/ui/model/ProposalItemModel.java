package com.offerfind.template.poc.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ugar on 11.02.16.
 */
public class ProposalItemModel implements Parcelable {

    private int id;
    private String title;
    private String description;
    private int footerHeight;

    public ProposalItemModel(int id, String title, String description, int footerHeight) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.footerHeight = footerHeight;
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

    public int getFooterHeight() {
        return footerHeight;
    }

    public void setFooterHeight(int footerHeight) {
        this.footerHeight = footerHeight;
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
        dest.writeInt(this.footerHeight);
    }

    protected ProposalItemModel(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.footerHeight = in.readInt();
    }

    public static final Parcelable.Creator<ProposalItemModel> CREATOR = new Parcelable.Creator<ProposalItemModel>() {
        public ProposalItemModel createFromParcel(Parcel source) {
            return new ProposalItemModel(source);
        }

        public ProposalItemModel[] newArray(int size) {
            return new ProposalItemModel[size];
        }
    };
}
