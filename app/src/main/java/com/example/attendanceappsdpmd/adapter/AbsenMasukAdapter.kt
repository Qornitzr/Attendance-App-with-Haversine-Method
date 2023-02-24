package com.example.attendanceappsdpmd.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceappsdpmd.holder.AbsenMasukViewHolder
import com.example.attendanceappsdpmd.data.AbsenMasuk
import com.example.attendanceappsdpmd.R
import java.util.*

class AbsenMasukAdapter(private val context: Context,
                  private val absenMasuk: ArrayList<AbsenMasuk?>?) : RecyclerView.Adapter<AbsenMasukViewHolder>() {
    private val listener: FirebaseDataListener
        get() {
            TODO()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsenMasukViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_absen_masuk, parent, false)
        return AbsenMasukViewHolder(view)
    }

    override fun onBindViewHolder(holder:  AbsenMasukViewHolder, position: Int) {
        holder.namaKaryawan.text = "Nama        : " + (absenMasuk?.get(position)?.masuk_Nama)
        holder.tanggal.text = "Tanggal     : " + absenMasuk?.get(position)?.masuk_Waktu
        holder.view.setOnClickListener { listener.onDataClick(absenMasuk?.get(position), position) }
    }

    override fun getItemCount(): Int {
        return absenMasuk?.size!!
    }

    //interface data listener
    interface FirebaseDataListener {
        fun onDataClick(absen: AbsenMasuk?, position: Int)
    }


}