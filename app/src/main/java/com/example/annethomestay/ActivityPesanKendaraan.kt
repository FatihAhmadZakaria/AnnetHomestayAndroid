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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class ActivityPesanKendaraan : AppCompatActivity() {
    private lateinit var binding: ActivityPesanKendaraanBinding
    private var durasiSewa: Int = 5 // Nilai awal durasi sewa
    private var hargaPerJam: Int = 20000 // Harga per jam kendaraan
    private val paymentMethods = mutableListOf<String>()
    private val paymentDetails = mutableListOf<String>()

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

        val img = intent.getStringExtra("img")
        val nama = intent.getStringExtra("nama")
        val harga = intent.getStringExtra("harga")

        val drawableId = resources.getIdentifier(img, "drawable", packageName)
        if (drawableId != 0) {
            binding.imgKen.setImageResource(drawableId)
        } else {
            binding.imgKen.setImageDrawable(null) // atau set gambar default jika tidak ditemukan
        }
        binding.namaKen.text = nama
        binding.hargaPesanKen.text = harga

        hargaPerJam = harga?.replace(".", "")?.toIntOrNull() ?: hargaPerJam

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        // Inisialisasi durasi awal
        binding.durasi.text = durasiSewa.toString()
        updateTotalBayar()

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
            i.putExtra("nama", nama)
            startActivity(i)
        }

        fetchPaymentMethods()
    }

    private fun setupPaymentSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.pengBayar.adapter = adapter

        binding.pengBayar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position >= 0 && position < paymentDetails.size) {
                    binding.detBank.visibility = View.VISIBLE
                    binding.bankAccountTextView.text = paymentDetails[position]
                } else {
                    binding.detBank.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun fetchPaymentMethods() {
        val apiService = ApiClient.apiService
        val call = apiService.getMetodeBayar()
        call.enqueue(object : Callback<List<MetodeBayar>> {
            override fun onResponse(call: Call<List<MetodeBayar>>, response: Response<List<MetodeBayar>>) {
                if (response.isSuccessful) {
                    response.body()?.let { methods ->
                        for (method in methods) {
                            paymentMethods.add(method.nama)
                            paymentDetails.add(method.no)
                        }
                        (binding.pengBayar.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@ActivityPesanKendaraan, "Failed to fetch payment methods", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MetodeBayar>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@ActivityPesanKendaraan, "Failed to fetch payment methods", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateDurasiTextView() {
        binding.durasi.text = durasiSewa.toString()
        updateTotalBayar()
    }

    private fun updateTotalBayar() {
        val totalBayar = durasiSewa * hargaPerJam
        val decimalFormat = DecimalFormat("#,##0.00")
        val formattedTotalBayar = decimalFormat.format(totalBayar / 100.0)
        binding.pengTotal.text = formattedTotalBayar
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
