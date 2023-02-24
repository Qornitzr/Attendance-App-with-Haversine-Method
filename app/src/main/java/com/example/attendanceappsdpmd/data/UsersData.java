package com.example.attendanceappsdpmd.data;



public class    UsersData {
    private String userId;
    private String nip;
    private String nama;
    private String email;
    private String password;
    private String handphone;
    private String alamat;
    private String gender;
    private String lahir;
    private String jabatan;
    private String golongan;
    private String imageUrl;

    public UsersData(String userId, String nip, String nama, String email, String password, String handphone, String alamat, String gender, String lahir, String jabatan, String golongan, String imageUrl) {
        this.userId = userId;
        this.nip = nip;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.handphone = handphone;
        this.alamat = alamat;
        this.gender = gender;
        this.lahir = lahir;
        this.jabatan = jabatan;
        this.golongan = golongan;
        this.imageUrl = imageUrl;
    }

    public UsersData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public String getHandphone() {
        return handphone;
    }

    public void setHandphone(String handphone) {
        this.handphone = handphone;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLahir() {
        return lahir;
    }

    public void setLahir(String lahir) {
        this.lahir = lahir;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getGolongan() {
        return golongan;
    }

    public void setGolongan(String golongan) {
        this.golongan = golongan;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}