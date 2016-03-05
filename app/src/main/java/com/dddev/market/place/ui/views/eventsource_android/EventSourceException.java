package com.dddev.market.place.ui.views.eventsource_android;

public class EventSourceException extends RuntimeException {
    public EventSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventSourceException(String message) {
        super(message);
    }
}
