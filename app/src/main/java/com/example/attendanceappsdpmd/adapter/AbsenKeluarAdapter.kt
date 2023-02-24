package com.example.attendanceappsdpmd.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceappsdpmd.R
import com.example.attendanceappsdpmd.data.AbsenKeluar
import com.example.attendanceappsdpmd.holder.AbsenKeluarViewHolder
import java.util.*

class AbsenKeluarAdapter(private val context: Context,
                         private val absenKeluar: ArrayList<AbsenKeluar?>?) : RecyclerView.Adapter<AbsenKeluarViewHolder>() {
    private val listener: FirebaseDataListener
        get() {
            TODO()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsenKeluarViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_absen_masuk, parent, false)
        return AbsenKeluarViewHolder(view)
    }

    override fun onBindViewHolder(holder:  AbsenKeluarViewHolder, position: Int) {
        holder.namaKaryawan.text = "Nama        : " + (absenKeluar?.get(position)?.keluar_Nama)
        holder.tanggal.text = "Tanggal     : " + absenKeluar?.get(position)?.keluar_Waktu
        holder.view.setOnClickListener { listener.onDataClick(absenKeluar?.get(position), position) }
    }

    override fun getItemCount(): Int {
        return absenKeluar?.size!!
    }

    //interface data listener
    interface FirebaseDataListener {
        fun onDataClick(absen: AbsenKeluar?, position: Int)
    }


}