package co.mrktplaces.android.core.api.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ugar on 04.05.16.
 */
public class BidRequest {
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private float price;
    @SerializedName("opportunityId")
    private int opportunityId;
    @SerializedName("ownerId")
    private int ownerId;

    public BidRequest(String description, float price, int opportunityId, int ownerId) {
        this.description = description;
        this.price = price;
        this.opportunityId = opportunityId;
        this.ownerId = ownerId;
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
