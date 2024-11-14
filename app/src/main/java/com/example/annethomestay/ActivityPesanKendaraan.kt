package com.example.annethomestay

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
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
import com.example.annethomestay.databinding.ActivityPesanKendaraanBinding
import java.util.Calendar

class ActivityPesanKendaraan : AppCompatActivity() {
    private lateinit var binding: ActivityPesanKendaraanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesanKendaraanBinding.inflate(layoutInflater)
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
        // Ambil data dari Intent
        val idKendaraan = intent.getStringExtra("id") ?: ""
        val imgKendaraan = intent.getStringArrayListExtra("img") ?: arrayListOf()
        val namaKendaraan = intent.getStringExtra("nama") ?: "Nama Kendaraan"
        val hargaKendaraan = intent.getStringExtra("harga") ?: "Harga"
        val deskripKendaraan = intent.getStringExtra("deskrip") ?: "Deskripsi Kendaraan"

        // Menampilkan nama kendaraan
        binding.kenNama.text = namaKendaraan

        // Menampilkan harga kendaraan
        binding.kenHarga.text = "Rp $hargaKendaraan /Hari"

        // Menampilkan deskripsi kendaraan
        binding.kenDeskrip.text = deskripKendaraan

        Log.d("KENDARAAN", "onCreate: $imgKendaraan")
        // Menampilkan gambar kendaraan dengan ViewFlipper
        val flipper: ViewFlipper = findViewById(R.id.img_kend)

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

        flipper.setFlipInterval(3000)
        flipper.startFlipping()

        // Pilih tanggal
        val tglCheckin = binding.motorMulai
        val tglCheckout = binding.motorSelesai
        tglCheckin.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                tglCheckin.text = selectedDate
                tglCheckin.setTextColor(Color.BLACK)
            }
        }
        tglCheckout.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                tglCheckout.text = selectedDate
                tglCheckout.setTextColor(Color.BLACK)
            }
        }
    }
    // Fungsi pilih tanggal
    @SuppressLint("DefaultLocale")
    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()

        // Set tanggal saat ini
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Buat DatePickerDialog
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Format tanggal yang dipilih
            val selectedDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
            onDateSelected(selectedDate)
        }, year, month, day)

        // Mengatur tanggal minimum untuk memilih
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() // Tidak dapat memilih tanggal sebelum hari ini

        // Tampilkan DatePickerDialog
        datePickerDialog.show()
    }
}