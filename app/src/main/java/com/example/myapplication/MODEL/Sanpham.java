package com.example.myapplication.MODEL;

import java.util.Map;

public class Sanpham {
    private String maLoai;
    private String masp;
    private String name;
    private double price;
    private int time_ship;
    private String describe;//mo ta
    private int luot_mua;//so luong
    private boolean trangThaiFavorite;
    private int favorite;
    private String imgURL;

    private String ten_loai;
    private String id_kh;
    private int starDanhGia;

    public Sanpham() {
    }


    public Sanpham(String maLoai, String masp, String name, double price, int time_ship, String describe, int luot_mua, boolean trangThaiFavorite, int favorite, String imgURL, String ten_loai, String id_kh, int starDanhGia) {
        this.maLoai = maLoai;
        this.masp = masp;
        this.name = name;
        this.price = price;
        this.time_ship = time_ship;
        this.describe = describe;
        this.luot_mua = luot_mua;
        this.trangThaiFavorite = trangThaiFavorite;
        this.favorite = favorite;
        this.imgURL = imgURL;
        this.ten_loai = ten_loai;
        this.id_kh = id_kh;
        this.starDanhGia = starDanhGia;
    }


    public boolean isTrangThaiFavorite() {
        return trangThaiFavorite;
    }

    public void setTrangThaiFavorite(boolean trangThaiFavorite) {
        this.trangThaiFavorite = trangThaiFavorite;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getId_kh() {
        return id_kh;
    }

    public void setId_kh(String id_kh) {
        this.id_kh = id_kh;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTime_ship() {
        return time_ship;
    }

    public void setTime_ship(int time_ship) {
        this.time_ship = time_ship;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getLuot_mua() {
        return luot_mua;
    }

    public void setLuot_mua(int luot_mua) {
        this.luot_mua = luot_mua;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }


    public String getTen_loai() {
        return ten_loai;
    }

    public void setTen_loai(String ten_loai) {
        this.ten_loai = ten_loai;
    }

    public int getStarDanhGia() {
        return starDanhGia;
    }

    public void setStarDanhGia(int starDanhGia) {
        this.starDanhGia = starDanhGia;
    }
}
