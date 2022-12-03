package com.example.myapplication.MODEL;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private String nguoiGui;
    private String nguoiNhan;
    private String  msg, timeChat, imgUrl;

    public ChatMessage() {
    }

    public ChatMessage(String nguoiGui, String nguoiNhan, String msg, String timeChat, String imgUrl) {
        this.nguoiGui = nguoiGui;
        this.nguoiNhan = nguoiNhan;
        this.msg = msg;
        this.timeChat = timeChat;
        this.imgUrl = imgUrl;
    }

    public String getTimeChat() {
        return timeChat;
    }

    public void setTimeChat(String timeChat) {
        this.timeChat = timeChat;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNguoiGui() {
        return nguoiGui;
    }

    public void setNguoiGui(String nguoiGui) {
        this.nguoiGui = nguoiGui;
    }

    public String getNguoiNhan() {
        return nguoiNhan;
    }

    public void setNguoiNhan(String nguoiNhan) {
        this.nguoiNhan = nguoiNhan;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
