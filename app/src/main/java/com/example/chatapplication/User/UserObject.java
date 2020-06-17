package com.example.chatapplication.User;

import java.io.Serializable;

public class UserObject implements Serializable {
    private String name, phone, Uid, notificationKey;
    private Boolean selected = false;

    public UserObject(String Uid) {
        this.Uid = Uid;
    }

    public UserObject(String Uid, String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.Uid = Uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUid() { return Uid; }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) { this.notificationKey = notificationKey; }

    public Boolean getSelected() { return selected; }

    public void setSelected(Boolean selected) { this.selected = selected; }
}
