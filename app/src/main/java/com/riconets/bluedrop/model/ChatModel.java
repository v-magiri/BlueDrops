package com.riconets.bluedrop.model;

public class ChatModel {
    String message,TimeStamp,Sender,Receiver;

    public ChatModel() {
    }

    public ChatModel(String message, String timeStamp, String sender, String receiver) {
        this.message = message;
        this.TimeStamp = timeStamp;
        this.Sender = sender;
        this.Receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }
}
