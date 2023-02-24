package com.example.attendanceappsdpmd.data;

public class AbsenKeluar {
    private String keluar_Nip;
    private String keluar_Nama;
    private String keluar_Waktu;

    public AbsenKeluar() {
    }

    public AbsenKeluar(String keluar_Nip, String keluar_Nama, String keluar_Time) {
        this.keluar_Nip = keluar_Nip;
        this.keluar_Nama = keluar_Nama;
        this.keluar_Waktu = keluar_Waktu;
    }

    public String getKeluar_Nip() {
        return keluar_Nip;
    }

    public void setKeluar_Nip(String keluar_Nip) {
        this.keluar_Nip = keluar_Nip;
    }

    public String getKeluar_Nama() {
        return keluar_Nama;
    }

    public void setKeluar_Nama(String keluar_Nama) {
        this.keluar_Nama = keluar_Nama;
    }

    public String getKeluar_Waktu() {
        return keluar_Waktu;
    }

    public void setKeluar_Time(String keluar_Time) {
        this.keluar_Waktu = keluar_Waktu;
    }
}
