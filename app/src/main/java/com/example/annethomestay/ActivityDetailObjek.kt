package com.example.annethomestay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ViewFlipper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.annethomestay.databinding.ActivityDetailObjekBinding

class ActivityDetailObjek : AppCompatActivity() {
    private lateinit var binding: ActivityDetailObjekBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailObjekBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgKendaraan = intent.getStringArrayListExtra("img") ?: arrayListOf()
        binding.nameObj.text = intent.getStringExtra("nama")
        binding.deskripObj.text = intent.getStringExtra("deskrip")
        val link = intent.getStringExtra("link")

        val flipper: ViewFlipper = findViewById(R.id.slider_objek)

        for (imgUrl in imgKendaraan) {
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

        flipper.setFlipInterval(3000) // Interval per slide
        flipper.startFlipping() // Mulai flipping gambar

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        binding.btObj.setOnClickListener {
            // Mendapatkan link Google Maps dari Intent
            val linkGoogleMaps = intent.getStringExtra("link")

            // Jika link tidak kosong, buka Google Maps
            if (!linkGoogleMaps.isNullOrEmpty()) {
                openGoogleMaps(linkGoogleMaps)
            } else {
                Log.e("ActivityPilihO", "Link Google Maps tidak ditemukan!")
            }
        }
    }
    private fun openGoogleMaps(link: String) {
        try {
            val uri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps") // Memastikan membuka aplikasi Google Maps
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("ActivityPilihO", "Error membuka Google Maps: ${e.message}")
        }
    }
}