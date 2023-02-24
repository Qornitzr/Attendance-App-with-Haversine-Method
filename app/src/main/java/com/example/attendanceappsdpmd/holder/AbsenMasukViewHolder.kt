package com.example.attendanceappsdpmd.holder

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceappsdpmd.R

class AbsenMasukViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @JvmField
    var namaKaryawan: TextView

    @JvmField
    var tanggal: TextView

    @JvmField
    var view: CardView

    init {
        namaKaryawan = itemView.findViewById(R.id.tvNama)
        tanggal = itemView.findViewById(R.id.tvTanggal)
        view = itemView.findViewById(R.id.cvAbsenMasuk)
    }
}