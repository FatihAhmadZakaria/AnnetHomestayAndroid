package com.example.annethomestay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

class DataListHomeAdapter(context: Context, private val arrayList: ArrayList<DataListHome>) :
    ArrayAdapter<DataListHome>(context, R.layout.list_home, arrayList) {

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

        // Ambil item pada posisi tertentu
        val item = arrayList[position]

        // Hanya set gambar pada item
//        Glide.with(context)
//            .load(item.img)  // Menggunakan URL gambar dari item
//            .into(viewHolder.imageView)

        if (item?.img?.isNotEmpty() == true) {
            Glide.with(context)
                .load(item.img[0])  // Memuat gambar pertama
                .placeholder(R.drawable.ic_home)  // Placeholder jika gagal
                .into(viewHolder.imageView)
        }

        return view
    }
}
