package com.example.annethomestay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

class DataListPenginapanAdapter (private val context: Context, private val arrayList: ArrayList<DataListPenginapan>) :
    ArrayAdapter<DataListPenginapan>(context, R.layout.list_home, arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_home, parent, false)
            viewHolder = ViewHolder()
            viewHolder.imageView = view.findViewById(R.id.img_list_home)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val item = arrayList[position]
        viewHolder.imageView.setImageResource(item.img)

        return view!!
    }
    private class ViewHolder {
        lateinit var imageView: ImageView
    }
}