package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.example.annethomestay.databinding.ActivityPromoBinding

class ActivityPromo : AppCompatActivity() {
    private lateinit var binding: ActivityPromoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPromoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Menerima data yang dikirim dari intent
        val imageId = intent.getIntExtra("img", 0) // 0 adalah nilai default jika tidak ada data yang dikirim
        val description = intent.getStringExtra("deskrip")

        // Menetapkan data ke ImageView dan TextView
        binding.imgPromo.setImageResource(imageId)
        binding.deskripPromo.text = description
        binding.icBackAbout.setOnClickListener {
            onBackPressed()
        }
    }
}