package com.offerfind.template.poc.core.api.strongloop;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.strongloop.android.loopback.Model;

import java.util.List;

/**
 * Created by ugar on 19.02.16.
 */
public class Opportunities extends Model {

    private List<ModelOpportunity> list;

    public Opportunities(String jsonObject) {
        Exclude exclude = new Exclude();
        Opportunities opportunity = new GsonBuilder().addDeserializationExclusionStrategy(exclude).addSerializationExclusionStrategy(exclude).create().fromJson(jsonObject, Opportunities.class);
        this.list = opportunity.getList();
    }

    public List<ModelOpportunity> getList() {
        return list;
    }

    public void setList(List<ModelOpportunity> list) {
        this.list = list;
    }

    public class ModelOpportunity {
        @SerializedName("id")
        private String id;
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;

        public ModelOpportunity(String id, String title, String description) {
            this.id = id;
            this.title = title;
            this.description = description;
        }

        public String getId() {
            return id;
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

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "ModelOpportunity{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}
