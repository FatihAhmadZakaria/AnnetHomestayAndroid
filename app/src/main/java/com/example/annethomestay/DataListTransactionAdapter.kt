package com.example.annethomestay


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.annethomestay.R


class DataListTransactionAdapter(context: Context, private val transaksiList: ArrayList<Any>) :
    ArrayAdapter<Any>(context, 0, transaksiList) {

    companion object {
        private const val TYPE_PENGINAPAN = 0
        private const val TYPE_KENDARAAN = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (transaksiList[position] is Penginapan) {
            TYPE_PENGINAPAN
        } else {
            TYPE_KENDARAAN
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewType = getItemViewType(position)
        if (view == null) {
            view = if (viewType == TYPE_PENGINAPAN) {
                LayoutInflater.from(context).inflate(R.layout.list_transaksi, parent, false)
            } else {
                LayoutInflater.from(context).inflate(R.layout.list_transaction_kendaraan, parent, false)
            }
        }

        val transaksi = getItem(position)
        if (viewType == TYPE_PENGINAPAN) {
            val penginapan = transaksi as Penginapan
            view!!.findViewById<TextView>(R.id.id_trans_peng).text = penginapan.idTransaksi
            view.findViewById<TextView>(R.id.jenis_trans_peng).text = penginapan.jenisTransaksi
            view.findViewById<TextView>(R.id.total_trans_peng).text = penginapan.totalTransaksi.toString()
            view.findViewById<TextView>(R.id.tgl_trans_peng).text = penginapan.tanggalSewa
        } else {
            val kendaraan = transaksi as Kendaraan
            view!!.findViewById<TextView>(R.id.id_trans).text = kendaraan.idTransaksi
            view.findViewById<TextView>(R.id.jenis_trans).text = kendaraan.jenisTransaksi
            view.findViewById<TextView>(R.id.total_trans).text = kendaraan.totalTransaksi.toString()
            view.findViewById<TextView>(R.id.tgl_trans).text = kendaraan.tanggalSewa
        }
        return view!!
    }
}
