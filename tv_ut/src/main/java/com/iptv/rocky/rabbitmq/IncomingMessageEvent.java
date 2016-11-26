package com.iptv.rocky.rabbitmq;

/**
 * 
 */
public class IncomingMessageEvent {

    private String message;

    public IncomingMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
