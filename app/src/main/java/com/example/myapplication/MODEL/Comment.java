package com.example.myapplication.MODEL;

import java.io.Serializable;

public class Comment implements Serializable {
    private String id_comment;
    private String id_user;
    private String name_user;
    private String img_user, content;
    private String time_comment;
    private int soSaoDanhGia;

    public Comment() {
    }

    public Comment(String id_comment, String id_user, String name_user, String img_user, String content, String time_comment, int soSaoDanhGia) {
        this.id_comment = id_comment;
        this.id_user = id_user;
        this.name_user = name_user;
        this.img_user = img_user;
        this.content = content;
        this.time_comment = time_comment;
        this.soSaoDanhGia = soSaoDanhGia;
    }

    public String getId_comment() {
        return id_comment;
    }

    public void setId_comment(String id_comment) {
        this.id_comment = id_comment;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime_comment() {
        return time_comment;
    }

    public void setTime_comment(String time_comment) {
        this.time_comment = time_comment;
    }

    public int getSoSaoDanhGia() {
        return soSaoDanhGia;
    }

    public void setSoSaoDanhGia(int soSaoDanhGia) {
        this.soSaoDanhGia = soSaoDanhGia;
    }
}
