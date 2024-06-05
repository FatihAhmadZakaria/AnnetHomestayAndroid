package com.example.annethomestay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        val img = intent.getStringExtra("img")
        val name = intent.getStringExtra("nama")
        val deskrip = intent.getStringExtra("deskrip")
        val link = intent.getStringExtra("link")

        val drawableId = resources.getIdentifier(img, "drawable", packageName)
        if (drawableId != 0) {
            binding.imgObj.setImageResource(drawableId)
        } else {
            binding.imgObj.setImageDrawable(null) // atau set gambar default jika tidak ditemukan
        }
        binding.nameObj.text = name
        binding.deskripObj.text = deskrip
        binding.btObj.setOnClickListener {
            val geo = Uri.parse(link)
            val i = Intent(Intent.ACTION_VIEW, geo)
            i.setPackage("com.google.android.apps.maps")
            i.resolveActivity(packageManager)?.let {
                startActivity(i)
            }
        }
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }
}