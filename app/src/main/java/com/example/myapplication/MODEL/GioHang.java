package com.example.myapplication.MODEL;

public class GioHang {
    private String  idUser;
    private String maSP, maLoai;
    private String  tenSanPham, hinhAnh;
    private int soLuong,luotBan;
    private double donGia;

    public GioHang() {
    }

    public GioHang(String idUser, String maSP, String maLoai, String tenSanPham, String hinhAnh, int soLuong, int luotBan, double donGia) {
        this.idUser = idUser;
        this.maSP = maSP;
        this.maLoai = maLoai;
        this.tenSanPham = tenSanPham;
        this.hinhAnh = hinhAnh;
        this.soLuong = soLuong;
        this.luotBan = luotBan;
        this.donGia = donGia;
    }

    public void setLuotBan(int luotBan) {
        this.luotBan = luotBan;
    }

    public int getLuotBan() {
        return luotBan;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }



    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }


    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
