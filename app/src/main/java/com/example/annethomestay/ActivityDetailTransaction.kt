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
        enableEdgeToEdge()
        binding = ActivityDetailTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val jenisTransaksi = intent.getStringExtra("jenis_transaksi")
        if (jenisTransaksi == "penginapan") {
            val transaksi = intent.getSerializableExtra("transaksi") as Penginapan?
            if (transaksi != null) {
                binding.idDetTrans.text = "ID Transaksi: ${transaksi.idTransaksi}"
                binding.jenisDetTgl.text = "Tanggal: ${transaksi.tanggalSewa}"
                binding.jenisDetTrans.text = "Jenis: ${transaksi.jenisTransaksi}"
                binding.jenisDetNama.text = "Nama: ${transaksi.namaPenginapan}"
                binding.jenisDetDurasi.text = "Durasi: ${transaksi.durasiSewa} hari"
                binding.jenisDetStatBayar.text = "Status Bayar: ${transaksi.statusPembayaran}"
            }
        } else if (jenisTransaksi == "kendaraan") {
            val transaksi = intent.getSerializableExtra("transaksi") as Kendaraan?
            if (transaksi != null) {
                binding.idDetTrans.text = "ID Transaksi: ${transaksi.idTransaksi}"
                binding.jenisDetTgl.text = "Tanggal: ${transaksi.tanggalSewa}"
                binding.jenisDetTrans.text = "Jenis: ${transaksi.jenisTransaksi}"
                binding.jenisDetNama.text = "Nama: ${transaksi.namaKendaraan}"
                binding.jenisDetDurasi.text = "Durasi: ${transaksi.durasiSewa} hari"
                binding.jenisDetStatBayar.text = "Status Bayar: ${transaksi.statusPembayaran}"
            }
        }
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }
}