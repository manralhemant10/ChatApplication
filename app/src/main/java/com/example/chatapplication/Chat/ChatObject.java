package com.example.chatapplication.Chat;

import com.example.chatapplication.User.UserObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatObject implements Serializable {

    public String chatId;

    private ArrayList<UserObject> userObjectArrayList = new ArrayList<>();

    public ChatObject(String chatId)
    {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public ArrayList<UserObject> getUserObjectArrayList() {
        return userObjectArrayList;
    }
    public void adduserToArrayList(UserObject mUser){
        userObjectArrayList.add(mUser);
    }
}
