package co.mrktplaces.android.ui.views.eventsource_android.impl;

public interface ConnectionHandler {
    void setReconnectionTimeMillis(long reconnectionTimeMillis);
    void setLastEventId(String lastEventId);
}
