package com.example.annethomestay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class DataListKendaraanAdapter (private val context: Context, private val arrayList: ArrayList<DataListKendaraan>) :
    ArrayAdapter<DataListKendaraan>(context, R.layout.list_kendaraan, arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_kendaraan, parent, false)
            viewHolder = ViewHolder()
            viewHolder.imageView = view.findViewById(R.id.img_ken)
            viewHolder.textNama = view.findViewById(R.id.nama_ken)
            viewHolder.textHarga = view.findViewById(R.id.harga_ken)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val item = arrayList[position]
        val drawableId = context.resources.getIdentifier(item.img, "drawable", context.packageName)
        viewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
        viewHolder.textNama.text = item.nama
        viewHolder.textHarga.text = item.harga

        return view!!
    }
    private class ViewHolder {
        lateinit var imageView: ImageView
        lateinit var textNama: TextView
        lateinit var textHarga: TextView
    }
}