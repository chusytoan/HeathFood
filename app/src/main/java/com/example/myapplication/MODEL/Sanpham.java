package com.example.myapplication.MODEL;



public class Sanpham {
    private String maLoai;
    private String masp;
    private String name;
    private double price;
    private int time_ship;
    private String describe;//mo ta
    private int favorite;
    private String imgURL;

    private String ten_loai;

    private int starDanhGia;

    public Sanpham() {
    }


    public Sanpham(String maLoai, String masp, String name, double price, int time_ship, String describe, int favorite, String imgURL, String ten_loai, int starDanhGia) {
        this.maLoai = maLoai;
        this.masp = masp;
        this.name = name;
        this.price = price;
        this.time_ship = time_ship;
        this.describe = describe;
        this.favorite = favorite;
        this.imgURL = imgURL;
        this.ten_loai = ten_loai;
        this.starDanhGia = starDanhGia;
    }





    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
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
