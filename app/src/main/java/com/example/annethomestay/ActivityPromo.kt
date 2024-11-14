package com.example.annethomestay

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.annethomestay.databinding.ActivityPromoBinding

class ActivityPromo : AppCompatActivity() {

    private lateinit var binding: ActivityPromoBinding
    private lateinit var imgUrls: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menerima data yang dikirim dari intent
        imgUrls = intent.getStringArrayListExtra("img_urls") ?: arrayListOf()
        val nama = intent.getStringExtra("nama")
        val description = intent.getStringExtra("deskrip")

        // Menampilkan nama dan deskripsi promo
        binding.namaPromo.text = nama
        binding.deskripPromo.text = description

        val flipper: ViewFlipper = findViewById(R.id.imgFlipper)

        for (imgUrl in imgUrls) {
            val imageView = ImageView(this)
            imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(this)
                .load(imgUrl)
                .into(imageView)
            flipper.addView(imageView)
        }

        flipper.setFlipInterval(3000)
        flipper.startFlipping()


        // Kembali ke halaman sebelumnya
        binding.icBackAbout.setOnClickListener {
            onBackPressed()
        }
    }
}
