package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityPilihKBinding

class ActivityPilihK : AppCompatActivity() {
    private lateinit var binding: ActivityPilihKBinding
    private lateinit var kenArrayList: ArrayList<DataListKendaraan>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihKBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        val img = arrayOf(R.drawable.kendaraan1, R.drawable.kendaraan2, R.drawable.kendaraan3)
        val nama = arrayOf("Vario", "Scoopy", "Beat")
        val harga = arrayOf("30000", "25000", "20000")
        val stat = arrayOf("Tersedia", "Tersedia", "Tidak Tersedia")

        kenArrayList = ArrayList()

        for ( i in img.indices){
            val ken = DataListKendaraan(img[i], nama[i], harga[i], stat[i])
            kenArrayList.add(ken)
        }

        binding.listKendaraan.isClickable = true
        binding.listKendaraan.adapter = DataListKendaraanAdapter(this, kenArrayList)
        binding.listKendaraan.setOnItemClickListener { parent, view, position, id ->
            val img = img[position]
            val nama = nama[position]
            val harga = harga[position]

            val i = Intent(this, ActivityPesanKendaraan::class.java)
            i.putExtra("img", img)
            i.putExtra("nama", nama)
            i.putExtra("harga", harga)
            startActivity(i)
        }
    }
}