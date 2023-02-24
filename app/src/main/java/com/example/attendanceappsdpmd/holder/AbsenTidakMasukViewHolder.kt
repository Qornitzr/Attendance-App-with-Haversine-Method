package com.example.attendanceappsdpmd.holder

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceappsdpmd.R

class AbsenTidakMasukViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @JvmField
    var namaKaryawan: TextView

    @JvmField
    var keterangan: TextView

    @JvmField

    var alasan: TextView

    @JvmField
    var view: CardView

    init {
        namaKaryawan = itemView.findViewById(R.id.tvNama)
        alasan = itemView.findViewById(R.id.tvAlasan)
        keterangan = itemView.findViewById(R.id.tvKeterangan)
        view = itemView.findViewById(R.id.cvAbsenTidakMasuk)

    }
}