package com.example.annethomestay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView

class DataListTransaksiAdapter(
    private val context: Context,
    private val transaksiList: ArrayList<Riwayat>,
    private val listener: FragmentTransaction
) : BaseAdapter() {

    override fun getCount(): Int {
        return transaksiList.size
    }

    override fun getItem(position: Int): Any {
        return transaksiList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_transaksi, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val transaksi = transaksiList[position]

        // Set data
        holder.namaProduk.text = transaksi.nama_produk
        holder.jenisTransaksi.text = transaksi.jenis
        holder.statusTransaksi.text = transaksi.status
        holder.totalTransaksi.text = context.getString(R.string.total_transaksi, transaksi.total)
        holder.tglTransaksi.text = transaksi.tgl_transaksi
        holder.tglMulai.text = transaksi.tgl_mulai
        holder.tglSelesai.text = transaksi.tgl_selesai

        // Logika visibilitas tombol
        holder.cardBayar.visibility = if (transaksi.status.equals("pending", true)) View.VISIBLE else View.GONE
        holder.cardBatal.visibility = if (transaksi.status.equals("Pending", true) || transaksi.status.equals("DP", true)) View.VISIBLE else View.GONE

        holder.cardBayar.setOnClickListener {
            listener.onBayarClicked(transaksi.snap_token)
        }

        holder.cardBatal.setOnClickListener {
            listener.onBatalkanClicked(transaksi.id_reservasi)
        }


        return view
    }

    private class ViewHolder(view: View) {
        val namaProduk: TextView = view.findViewById(R.id.nama_produk)
        val jenisTransaksi: TextView = view.findViewById(R.id.jenis_transaksi)
        val statusTransaksi: TextView = view.findViewById(R.id.status_transaksi)
        val totalTransaksi: TextView = view.findViewById(R.id.total_transaksi)
        val tglTransaksi: TextView = view.findViewById(R.id.tgl_transaksi)
        val tglMulai: TextView = view.findViewById(R.id.tgl_mulai)
        val tglSelesai: TextView = view.findViewById(R.id.tgl_selesai)
        val cardBayar: CardView = view.findViewById(R.id.card_bayar)
        val cardBatal: CardView = view.findViewById(R.id.card_batal)
    }
}
