package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityPesanKendaraanBinding

class ActivityPesanKendaraan : AppCompatActivity() {
    private lateinit var binding: ActivityPesanKendaraanBinding
    private var durasiSewa: Int = 5 // Nilai awal durasi sewa

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
        setupPaymentSpinner()

        val img = intent.getIntExtra("img", 0)
        val nama = intent.getStringExtra("nama")
        val harga = intent.getStringExtra("harga")

        binding.imgKen.setImageResource(img)
        binding.namaKen.text = nama
        binding.hargaPesanKen.text = harga

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        // Inisialisasi durasi awal
        binding.durasi.text = durasiSewa.toString()

        // Setup tombol plus dan minus
        binding.plus.setOnClickListener {
            durasiSewa++
            updateDurasiTextView()
            showToast()
        }

        binding.min.setOnClickListener {
            if (durasiSewa > 1) {
                durasiSewa--
                updateDurasiTextView()
                showToast()
            } else {
                Toast.makeText(this, "Durasi sewa tidak bisa kurang dari 1 jam", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up button to show accommodation dialog
        binding.btAntar.setOnClickListener {
            showAccommodationDialog()
        }
        binding.btPesanKen.setOnClickListener {
            val i = Intent(this, ActivityPayment::class.java)
            startActivity(i)
        }
    }

    private fun setupPaymentSpinner() {
        val paymentMethods = arrayOf("Full di tempat", "Transfer dengan no rekening")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.pengBayar.adapter = adapter

        binding.pengBayar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                binding.detBank.visibility = if (position == 1) View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun updateDurasiTextView() {
        binding.durasi.text = durasiSewa.toString()
    }

    private fun showToast() {
        Toast.makeText(this, "Durasi sewa: $durasiSewa jam", Toast.LENGTH_SHORT).show()
    }

    private fun showAccommodationDialog() {
        val accommodations = arrayOf(
            "Limasan", "Gladag", "Teratai",
            "Glamping 1", "Glamping 2", "Glamping 3",
            "Glamping 4", "Glamping 5", "Glamping 6",
            "Glamping 7", "Glamping 8", "Glamping 9", "Glamping 10"
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Penginapan")
        builder.setItems(accommodations) { dialog, which ->
            Toast.makeText(this, "Anda memilih: ${accommodations[which]}", Toast.LENGTH_SHORT).show()
            binding.btAntar.text = accommodations[which]
        }
        builder.setNegativeButton("Batal", null)
        builder.create().show()
    }
}
