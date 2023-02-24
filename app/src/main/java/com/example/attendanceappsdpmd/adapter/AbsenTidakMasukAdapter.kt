package com.example.attendanceappsdpmd.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceappsdpmd.holder.AbsenMasukViewHolder
import com.example.attendanceappsdpmd.data.AbsenMasuk
import com.example.attendanceappsdpmd.R
import com.example.attendanceappsdpmd.data.AbsenTidakMasuk
import com.example.attendanceappsdpmd.holder.AbsenTidakMasukViewHolder
import java.util.*

class AbsenTidakMasukAdapter(private val context: Context,
                             private val absenTidakMasuk: ArrayList<AbsenTidakMasuk?>?) : RecyclerView.Adapter<AbsenTidakMasukViewHolder>() {
    private val listener: FirebaseDataListener
        get() {
            TODO()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsenTidakMasukViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_absen_tidak_masuk, parent, false)
        return AbsenTidakMasukViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbsenTidakMasukViewHolder, position: Int) {
        holder.namaKaryawan.text = "Nama            : " + (absenTidakMasuk?.get(position)?.tidakMasuk_Nama)
        holder.keterangan.text = "Keterangan    : " + (absenTidakMasuk?.get(position)?.tidakMasuk_Keterangan)
        holder.alasan.text = "Alasan            : " + (absenTidakMasuk?.get(position)?.tidakMasuk_Alasan)
        holder.view.setOnClickListener { listener.onDataClick(absenTidakMasuk?.get(position), position) }
    }

    override fun getItemCount(): Int {
        return absenTidakMasuk?.size!!
    }

    //interface data listener
    interface FirebaseDataListener {
        fun onDataClick(absen: AbsenTidakMasuk?, position: Int)
    }




}