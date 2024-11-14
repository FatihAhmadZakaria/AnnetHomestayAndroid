package com.example.annethomestay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

class DataListPenginapanAdapter(context: Context, private val arrayList: ArrayList<DataListPenginapan>) :
    ArrayAdapter<DataListPenginapan>(context, R.layout.list_home, arrayList) {

    private class ViewHolder(view: View) {
        val imageView: ImageView = view.findViewById(R.id.img_list_home)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_home, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        // Mendapatkan item pada posisi tertentu
        val item = arrayList[position]

        // Memuat gambar pertama dari daftar gambar dalam item menggunakan Glide
        if (item.img.isNotEmpty()) {
            Glide.with(context)
                .load(item.img[0])  // Menggunakan gambar pertama dalam daftar
                .placeholder(R.drawable.ic_home)  // Placeholder jika gambar gagal dimuat
                .into(viewHolder.imageView)
        } else {
            viewHolder.imageView.setImageResource(R.drawable.ic_home)  // Gambar default jika daftar kosong
        }

        return view
    }
}
