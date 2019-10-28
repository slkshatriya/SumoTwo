package com.one4all.sumotwo;

import java.io.Serializable;

public class ChatLog implements Serializable {
    private String uid;
    private String userName;
    private String imageUrl;

    public ChatLog(String uid, String userName, String imageUrl) {
        this.uid = uid;
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    public ChatLog() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
