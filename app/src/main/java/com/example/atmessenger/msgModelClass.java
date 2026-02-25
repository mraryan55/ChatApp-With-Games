package com.example.atmessenger;

public class msgModelClass {

    private String message;
    private String senderid;
    private long timeStamp;

    // ✅ Added for deletion and Firebase tracking
    private String messageId;
    private String senderRoom;

    public msgModelClass() {
        // Default constructor required for Firebase
    }

    public msgModelClass(String message, String senderid, long timeStamp, String messageId, String senderRoom) {
        this.message = message;
        this.senderid = senderid;
        this.timeStamp = timeStamp;
        this.messageId = messageId;
        this.senderRoom = senderRoom;
    }

    public msgModelClass(String message, String senderUID, long time) {
        this.message = message;
        this.senderid = senderUID;
        this.timeStamp = time;
    }

    // ✅ Getters and Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderRoom() {
        return senderRoom;
    }

    public void setSenderRoom(String senderRoom) {
        this.senderRoom = senderRoom;
    }
}