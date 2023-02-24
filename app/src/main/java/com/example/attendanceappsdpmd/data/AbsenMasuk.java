package com.example.attendanceappsdpmd.data;

public class AbsenMasuk {
    private String masuk_Nip;
    private String masuk_Nama;
    private String masuk_Waktu;

    public AbsenMasuk() {
    }

    public AbsenMasuk(String masuk_Nip, String masuk_Nama, String masuk_Time) {
        this.masuk_Nip = masuk_Nip;
        this.masuk_Nama = masuk_Nama;
        this.masuk_Waktu = masuk_Waktu;
    }

    public String getMasuk_Nip() {
        return masuk_Nip;
    }

    public void setMasuk_Nip(String masuk_Nip) {
        this.masuk_Nip = masuk_Nip;
    }

    public String getMasuk_Nama() {
        return masuk_Nama;
    }

    public void setMasuk_Nama(String masuk_Nama) {
        this.masuk_Nama = masuk_Nama;
    }

    public String getMasuk_Waktu() {
        return masuk_Waktu;
    }

    public void setMasuk_Waktu(String masuk_Time) {
        this.masuk_Waktu = masuk_Time;
    }
}

