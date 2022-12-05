package com.example.myapplication.MODEL;

public class SanphamFavorite {

    private String  idUser;
    private String maSP, maLoai,mota;
    private String  tenSanPham, ten_loai,hinhAnh;
    private double donGia;
    private int time_ship,favorite,starDanhGia;


    public SanphamFavorite(String idUser, String maSP, String maLoai, String mota, String tenSanPham, String ten_loai, String hinhAnh, double donGia, int time_ship, int favorite, int starDanhGia) {
        this.idUser = idUser;
        this.maSP = maSP;
        this.maLoai = maLoai;
        this.mota = mota;
        this.tenSanPham = tenSanPham;
        this.ten_loai = ten_loai;
        this.hinhAnh = hinhAnh;
        this.donGia = donGia;
        this.time_ship = time_ship;
        this.favorite = favorite;
        this.starDanhGia = starDanhGia;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getStarDanhGia() {
        return starDanhGia;
    }

    public void setStarDanhGia(int starDanhGia) {
        this.starDanhGia = starDanhGia;
    }

    public SanphamFavorite() {
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getTime_ship() {
        return time_ship;
    }

    public void setTime_ship(int time_ship) {
        this.time_ship = time_ship;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getTen_loai() {
        return ten_loai;
    }

    public void setTen_loai(String ten_loai) {
        this.ten_loai = ten_loai;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
}
