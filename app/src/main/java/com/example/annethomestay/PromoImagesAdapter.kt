package com.example.annethomestay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PromoImagesAdapter(private val context: Context, private val imageList: List<Image>) :
    RecyclerView.Adapter<PromoImagesAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_home, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imageList[position]
        val imageUrl = "http://127.0.0.1:8000/api/images/${image.img_path}"
        Glide.with(context).load(imageUrl).into(holder.imageView) // Use Glide for image loading
    }

    override fun getItemCount(): Int = imageList.size

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_list_home)
    }
}
