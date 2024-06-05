package com.example.annethomestay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.content.ContextCompat

class DataListHomeAdapter(private val context: Context, private val arrayList: ArrayList<DataListHome>) :
    ArrayAdapter<DataListHome>(context, R.layout.list_home, arrayList) {

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
        val drawableId = context.resources.getIdentifier(item.img, "drawable", context.packageName)
        viewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId))

        return view!!
    }

    private class ViewHolder {
        lateinit var imageView: ImageView
    }
}