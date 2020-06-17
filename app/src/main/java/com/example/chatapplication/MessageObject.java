package com.example.chatapplication;

import java.util.ArrayList;

public class MessageObject {

    String messageId, senderId, message_s ;
    ArrayList<String> mediaUrlList;

    public MessageObject(String messageId, String senderId, String message_s, ArrayList<String> mediaUrlList)
    {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message_s = message_s;
        this.mediaUrlList = mediaUrlList;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message_s;
    }

    public ArrayList<String> getMediaUrlList() { return mediaUrlList; }
}
