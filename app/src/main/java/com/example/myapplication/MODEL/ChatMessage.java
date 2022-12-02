package com.example.myapplication.MODEL;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private String id_user;
    private String name_user;
    private String img_user, messager;
    private String time_chat;

    public ChatMessage(String id_user, String name_user, String img_user, String messager, String time_chat) {
        this.id_user = id_user;
        this.name_user = name_user;
        this.img_user = img_user;
        this.messager = messager;
        this.time_chat = time_chat;
    }

    public ChatMessage() {
    }

    public String getMessager() {
        return messager;
    }

    public void setMessager(String messager) {
        this.messager = messager;
    }


    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getImg_user() {
        return img_user;
    }

    public void setImg_user(String img_user) {
        this.img_user = img_user;
    }



    public String getTime_chat() {
        return time_chat;
    }

    public void setTime_chat(String time_chat) {
        this.time_chat = time_chat;
    }
}
