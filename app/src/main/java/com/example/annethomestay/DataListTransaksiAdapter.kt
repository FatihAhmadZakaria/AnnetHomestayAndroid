package com.example.annethomestay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class DataListTransaksiAdapter(context: Context, private val transaksiList: List<DataListTransaksi>) :
    ArrayAdapter<DataListTransaksi>(context, 0, transaksiList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val transaksi = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_transaksi, parent, false)

        val idTransPeng: TextView = view.findViewById(R.id.id_riwayet)
        val jenisTransPeng: TextView = view.findViewById(R.id.jenis_trans_peng)
        val statTransPeng: TextView = view.findViewById(R.id.stat_trans_peng)
        val totalTransPeng: TextView = view.findViewById(R.id.total_trans_peng)
        val tglTransPeng: TextView = view.findViewById(R.id.tgl_trans_peng)

        transaksi?.let {
            idTransPeng.text = it.id.toString()
            jenisTransPeng.text = it.jenis
            statTransPeng.text = it.stat
            totalTransPeng.text = it.durasi.toString()
            tglTransPeng.text = it.tgl
        }

        return view
    }
}