package com.example.annethomestay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class DataListRekomendasiAdapter(private val context: Context, private val arrayList: ArrayList<DataListRekomendasi>) :
    ArrayAdapter<DataListRekomendasi>(context, R.layout.list_rek_obj, arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_rek_obj, parent, false)
            viewHolder = ViewHolder()
            viewHolder.imageView = view.findViewById(R.id.img_rek_obj)
            viewHolder.textView = view.findViewById(R.id.name_rek_obj)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val item = arrayList[position]
        val drawableId = context.resources.getIdentifier(item.imgObj, "drawable", context.packageName)
        viewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
        viewHolder.textView.text = item.nameObj

        return view!!
    }
    private class ViewHolder {
        lateinit var imageView: ImageView
        lateinit var textView: TextView
    }
}