package com.example.attendanceappsdpmd.data;

public class AbsenTidakMasuk {
    private String tidakMasuk_Nip;
    private String tidakMasuk_Nama;
    private String tidakMasuk_Keterangan;
    private String tidakMasuk_Alasan;

    public AbsenTidakMasuk() {
    }

    public AbsenTidakMasuk(String tidakMasuk_Nip, String tidakMasuk_Nama, String tidakMasuk_Keterangan, String tidakMasuk_Alasan) {
        this.tidakMasuk_Nip = tidakMasuk_Nip;
        this.tidakMasuk_Nama = tidakMasuk_Nama;
        this.tidakMasuk_Keterangan = tidakMasuk_Keterangan;
        this.tidakMasuk_Alasan = tidakMasuk_Alasan;
    }

    public String getTidakMasuk_Nip() {
        return tidakMasuk_Nip;
    }

    public void setTidakMasuk_Nip(String tidakMasuk_Nip) {
        this.tidakMasuk_Nip = tidakMasuk_Nip;
    }

    public String getTidakMasuk_Nama() {
        return tidakMasuk_Nama;
    }

    public void setTidakMasuk_Nama(String tidakMasuk_Nama) {
        this.tidakMasuk_Nama = tidakMasuk_Nama;
    }

    public String getTidakMasuk_Keterangan() {
        return tidakMasuk_Keterangan;
    }

    public void setTidakMasuk_Keterangan(String tidakMasuk_Keterangan) {
        this.tidakMasuk_Keterangan = tidakMasuk_Keterangan;
    }

    public String getTidakMasuk_Alasan() {
        return tidakMasuk_Alasan;
    }

    public void setTidakMasuk_Alasan(String tidakMasuk_Alasan) {
        this.tidakMasuk_Alasan = tidakMasuk_Alasan;
    }
}
