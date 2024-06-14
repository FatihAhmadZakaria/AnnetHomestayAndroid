package com.example.annethomestay

import TransaksiResponse
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.annethomestay.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class ActivityPesanKendaraan : AppCompatActivity() {
    private lateinit var binding: ActivityPesanKendaraanBinding
    private var durasiSewa: Int = 5
    private var hargaPerJam: Int = 20000
    private val paymentMethods = mutableListOf<String>()
    private val paymentDetails = mutableListOf<String>()
    private var selectedAccommodationId: Int =
        -1
    private var selectedPaymentMethodId: Long =
        -1

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
                Toast.makeText(this, "Durasi sewa tidak bisa kurang dari 1 jam", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Set up button to show accommodation dialog
        binding.btAntar.setOnClickListener {
            showAccommodationDialog()
        }

        binding.btPesanKen.setOnClickListener {
            val durasi = durasiSewa // Misalnya, mengambil nilai durasi dari variabel durasiSewa
            val totalBayar =
                hargaPerJam * durasi // Menghitung total bayar berdasarkan harga per jam dan durasi
            val accommodationId =
                selectedAccommodationId // Mengambil accommodationId yang telah dipilih dari dialog
            val paymentMethodId =
                selectedPaymentMethodId // Mengambil paymentMethodId yang telah dipilih dari spinner

            pesanKendaraan(durasi, totalBayar, accommodationId, paymentMethodId)
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

                    // Mendapatkan ID metode pembayaran berdasarkan posisi dalam daftar
                    selectedPaymentMethodId = getPaymentMethodId(position)
                } else {
                    binding.detBank.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }
    private fun getPaymentMethodId(position: Int): Long {
        // Jika posisi 0, kembalikan 1 sebagai metode pembayaran default
        return if (position == 0) {
            1
        } else {
            // Jika posisi bukan 0, kembalikan posisi + 1
            position + 1L
        }
    }

    private fun fetchPaymentMethods() {
        val apiService = ApiClient.apiService
        val call = apiService.getMetodeBayar()
        call.enqueue(object : Callback<List<MetodeBayar>> {
            override fun onResponse(
                call: Call<List<MetodeBayar>>,
                response: Response<List<MetodeBayar>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { methods ->
                        for (method in methods) {
                            paymentMethods.add(method.nama)
                            paymentDetails.add(method.no)
                        }
                        (binding.pengBayar.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(
                        this@ActivityPesanKendaraan,
                        "Failed to fetch payment methods",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<MetodeBayar>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@ActivityPesanKendaraan,
                    "Failed to fetch payment methods",
                    Toast.LENGTH_SHORT
                ).show()
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
            Toast.makeText(this, "Anda memilih: ${accommodations[which]}", Toast.LENGTH_SHORT)
                .show()
            binding.btAntar.text = accommodations[which]
            selectedAccommodationId = which + 1 // Menyimpan nilai ID penginapan yang dipilih
        }
        builder.setNegativeButton("Batal", null)
        builder.create().show()
    }

    private fun pesanKendaraan(
        durasi: Int,
        totalBayar: Int,
        accommodationId: Int,
        paymentMethodId: Long
    ) {
        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId()
        val kenId = intent.getIntExtra("id", 0)

        // Mengurangi dua nol dari totalBayar
        val totalBayarReduced = reduceTwoZeros(totalBayar)

        val data = TransaksiKendaraan(
            id_k = kenId,
            id_p = accommodationId,
            id_mtd = paymentMethodId,
            user_id = userId,
            durasi_trans_k = durasi,
            stat_trans_k = "pending",
            total_bayar = totalBayarReduced // Gunakan nilai totalBayar yang sudah dikurangi dua nol
        )
        val apiService = ApiClient.apiService
        apiService.transaksiKendaraan(data).enqueue(object : Callback<TransaksiResponse> {
            override fun onResponse(
                call: Call<TransaksiResponse>,
                response: Response<TransaksiResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        // Berhasil memesan kendaraan, pindah ke halaman ActivityPayment
                        val intent = Intent(this@ActivityPesanKendaraan, ActivityPayment::class.java)
                        intent.putExtra("nama_penginapan", binding.btAntar.text.toString()) // Mengirim nama penginapan
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@ActivityPesanKendaraan,
                            "Gagal memesan kendaraan: Response body is null",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@ActivityPesanKendaraan,
                        "Gagal memesan kendaraan: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Gagal disini dan pesan errornya internal server error, padahal jika data input lewat postman dengan rawjson bisa
                }
            }

            override fun onFailure(call: Call<TransaksiResponse>, t: Throwable) {
                Log.d("Error Transaksi kendaraan", "pesanKendaraan: ${t.message}")
                Toast.makeText(
                    this@ActivityPesanKendaraan,
                    "Terjadi kesalahan: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun reduceTwoZeros(totalBayar: Int): Int {
        return totalBayar / 100 // Mengurangi dua digit nol dari belakang
    }

    private fun getPaymentMethodId(paymentMethodName: String): Long {
        return 1
    }
}