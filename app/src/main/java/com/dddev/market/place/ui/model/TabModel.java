package com.dddev.market.place.ui.model;

/**
 * Created by ugar on 12.02.16.
 */
public class TabModel {

    private int id;
    private int icon;

    public TabModel(int id, int icon) {
        this.id = id;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
