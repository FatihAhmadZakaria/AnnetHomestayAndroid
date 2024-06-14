package com.example.annethomestay

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityDetailTransactionBinding

class ActivityDetailTransaction : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("id_trans", 0).toString()
        val tgl = intent.getStringExtra("tgl")
        val jenis = intent.getStringExtra("jenis")
        val nama = intent.getStringExtra("nama")
        val durasi = intent.getIntExtra("durasi", 0).toString()
        val stat = intent.getStringExtra("stat")

        binding.idDetTrans.text = id
        binding.jenisDetTgl.text = tgl
        binding.jenisDetTrans.text = jenis
        binding.jenisDetNama.text = nama
        binding.jenisDetDurasi.text = durasi
        binding.jenisDetStatBayar.text = stat

        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }
}