package com.example.myapplication.MODEL;

import java.util.Map;

public class User {
    private String id;
    private String name, email, password, imgURL;
    private boolean trangThaiTym;
    private int LoaiUser, soSaoDanhGia;

    public User() {
    }

    public User(String id, String name, String email, String password, String imgURL, boolean trangThaiTym, int loaiUser, int soSaoDanhGia) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.imgURL = imgURL;
        this.trangThaiTym = trangThaiTym;
        LoaiUser = loaiUser;
        this.soSaoDanhGia = soSaoDanhGia;
    }

    public int getSoSaoDanhGia() {
        return soSaoDanhGia;
    }

    public void setSoSaoDanhGia(int soSaoDanhGia) {
        this.soSaoDanhGia = soSaoDanhGia;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public boolean isTrangThaiTym() {
        return trangThaiTym;
    }

    public void setTrangThaiTym(boolean trangThaiTym) {
        this.trangThaiTym = trangThaiTym;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoaiUser() {
        return LoaiUser;
    }

    public void setLoaiUser(int loaiUser) {
        LoaiUser = loaiUser;
    }
}
