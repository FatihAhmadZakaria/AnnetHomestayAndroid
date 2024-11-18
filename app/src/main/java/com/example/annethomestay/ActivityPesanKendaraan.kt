package com.example.annethomestay

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.annethomestay.ApiClient.apiService
import com.example.annethomestay.databinding.ActivityPesanKendaraanBinding
import com.example.annethomestay.utils.SessionManager
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

class ActivityPesanKendaraan : AppCompatActivity() {
    private lateinit var binding: ActivityPesanKendaraanBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var checkinDate: LocalDate? = null
    private var checkoutDate: LocalDate? = null
    private var jumlahSewa: Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
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

        UiKitApi.Builder()
            .withMerchantClientKey("SB-Mid-client-rTMPU8glYQjPkdDQ") // Client key dari Midtrans
            .withContext(this) // Konteks aktivitas
            .withMerchantUrl("https://45e4-2001-448a-4040-9ce4-3029-1922-5aa6-9303.ngrok-free.app/api/midtrans/callback/") // Ganti dengan URL server untuk menangani callback
            .enableLog(true) // Aktifkan log SDK
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // Tema warna
            .build()

        setLocaleNew("en")

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                    Toast.makeText(this, "Transaction ID: ${transactionResult?.transactionId}", Toast.LENGTH_LONG).show() // Tampilkan ID transaksi
                }
            }
        }

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        val idUser = SessionManager(this).getUserId()
        val idProduk = intent.getIntExtra("id",0)
        val tipeProduk = "kendaraan"
        val imgKendaraan = intent.getStringArrayListExtra("img") ?: arrayListOf()
        val namaKendaraan = intent.getStringExtra("nama") ?: "Nama Kendaraan"
        val hargaKendaraan = intent.getIntExtra("harga", 0)
        val deskripKendaraan = intent.getStringExtra("deskrip") ?: "Deskripsi Kendaraan"
        SessionManager(this@ActivityPesanKendaraan).saveIdKendaraan(idProduk)

        binding.kenNama.text = namaKendaraan
        binding.kenHarga.text = "Rp $hargaKendaraan"
        binding.kenDeskrip.text = deskripKendaraan

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

                // Simpan tanggal check-in ke variabel
                checkinDate = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))

                // Hitung jumlah hari jika checkoutDate sudah dipilih
                updateHarga()
                SessionManager(this@ActivityPesanKendaraan).saveTglMulai(selectedDate)
            }
        }

        tglCheckout.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                tglCheckout.text = selectedDate
                tglCheckout.setTextColor(Color.BLACK)

                // Simpan tanggal check-out ke variabel
                checkoutDate = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))

                // Hitung jumlah hari jika checkinDate sudah dipilih
                updateHarga()
                SessionManager(this@ActivityPesanKendaraan).saveTglSelesai(selectedDate)
                Toast.makeText(this@ActivityPesanKendaraan, selectedDate, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btPesanKen.setOnClickListener {
            updateHarga()
            val jumlahPesan = jumlahSewa
            val reservasiRequest = ReservasiRequest(
                id_user = idUser,
                id_produk = idProduk,
                tipe_produk = tipeProduk,
                tgl_mulai = tglCheckin.text.toString(),
                tgl_selesai = tglCheckout.text.toString(),
                jumlah_pesan = jumlahPesan
            )

            // Mengirimkan request menggunakan Retrofit
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response: Response<ReservasiResponse> = apiService.createReservasi(reservasiRequest)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            Log.d("API Response", "Response: $responseBody") // Menampilkan seluruh response
                            Log.d("API Response", "Response Code: ${response.code()}") // Menampilkan status code
                            if (responseBody?.success == true) {
                                Toast.makeText(this@ActivityPesanKendaraan, "Reservasi berhasil", Toast.LENGTH_SHORT).show()
                                SessionManager(this@ActivityPesanKendaraan).saveReservasiId(responseBody.id_reservasi)
                                val reservasiId = SessionManager(this@ActivityPesanKendaraan).getReservasiId()

                                Log.d("Reservasi ID", "ID Reservasi yang disimpan: $reservasiId")
                                bayarReservasi()
                            } else {
                                Toast.makeText(this@ActivityPesanKendaraan, "Gagal melakukan reservasi", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e("API Error", "Response Code: ${response.code()} - ${response.message()}")
                            Toast.makeText(this@ActivityPesanKendaraan, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }

                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        // Jika terjadi exception
                        Toast.makeText(this@ActivityPesanKendaraan, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
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

    private fun updateHarga() {
        // Mengecek apakah checkinDate dan checkoutDate tidak null
        val daysBetween = if (checkinDate != null && checkoutDate != null) {
            // Menghitung selisih hari jika kedua tanggal ada
            ChronoUnit.DAYS.between(checkinDate, checkoutDate).toInt()
        } else {
            // Jika salah satu atau kedua tanggal tidak ada, set default menjadi 1
            1
        }

        if (daysBetween < 0) {
            Toast.makeText(this, "Tanggal check-out harus setelah check-in!", Toast.LENGTH_SHORT).show()
        } else {
            // Menampilkan jumlah hari menginap
            Toast.makeText(this, "Jumlah hari menginap: $daysBetween hari", Toast.LENGTH_SHORT).show()
        }

        // Mengambil harga dari intent dan menghitung jumlah DP
        val harga = intent.getIntExtra("harga", 0)
        val jumlahDpSatuan = (harga * 0.1).toInt()
        val jumlahDp = jumlahDpSatuan * daysBetween
        jumlahSewa = daysBetween // Menyimpan jumlah sewa di tingkat kelas

        // Menampilkan total harga
        binding.pengTotal.text = jumlahDp.toString()

        val tglCI = SessionManager(this@ActivityPesanKendaraan).getTglMulai()
        val tglCO = SessionManager(this@ActivityPesanKendaraan).getTglSelesai()

        val idReservasi = SessionManager(this@ActivityPesanKendaraan).getIdKendaraan()
        cekKetersediaan(idProperti = idReservasi, tglMulai = tglCI, tglSelesai = tglCO)

    }

    private fun bayarReservasi() {
        updateHarga()
        // Ambil harga dari Intent
        val harga = intent.getIntExtra("harga", 0)  // Default ke 0 jika tidak ada harga

        // Hitung jumlah DP (10% dari harga)
        val jumlahDpSatuan = (harga * 0.1).toInt()
        val jumlahDp = jumlahDpSatuan * jumlahSewa

        // Ambil id_reservasi dari SessionManager atau response sebelumnya
        val idReservasi = SessionManager(this@ActivityPesanKendaraan).getReservasiId()

        if (idReservasi == 0) {
            Toast.makeText(this, "ID Reservasi tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        // Membuat request body dengan jumlah_dp
        val bayarRequest = BayarRequest(jumlah_dp = jumlahDp)
        Log.d("MidTot", jumlahDp.toString())
        Log.d("MidId", idReservasi.toString())

        // Mengirimkan request POST menggunakan Retrofit
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Membuat request ke endpoint /api/reservasi/{id_reservasi}/bayar
                val response = apiService.bayarReservasi(id_reservasi = idReservasi, bayarRequest = bayarRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody?.success == true) {
                            Toast.makeText(this@ActivityPesanKendaraan, "Pembayaran berhasil", Toast.LENGTH_SHORT).show()
                            startPayment(responseBody.snap_token)
                        } else {
                            Toast.makeText(this@ActivityPesanKendaraan, "Pembayaran gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ActivityPesanKendaraan, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Jika terjadi exception
                    Toast.makeText(this@ActivityPesanKendaraan, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startPayment(snapToken: String) {
        UiKitApi.getDefaultInstance().startPaymentUiFlow(
            this@ActivityPesanKendaraan, // Aktivitas
            launcher, // ActivityResultLauncher
            snapToken // Snap Token dari Midtrans
        )
    }


    fun cekKetersediaan(idProperti: Int, tglMulai: String, tglSelesai: String) {
        // Menggunakan CoroutineScope untuk menjalankan operasi di background thread (IO)
        Log.d("IDCEK", idProperti.toString())
        Log.d("MULAICEK", tglMulai)
        Log.d("SELESAICEK", tglSelesai)
        CoroutineScope(Dispatchers.IO).launch {
            val request = CekKetersediaanRequest(tglMulai, tglSelesai)

            try {
                // Melakukan request menggunakan Retrofit (harus dilakukan di background thread)
                val response: Response<CekKetersediaanResponseKen> = apiService.cekKetersediaanKendaraan(request, idProperti)

                // Menangani response di thread utama
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            if (responseBody.success) {
                                // Menampilkan ketersediaan jika sukses
                                Log.d("CekKetersediaan", "Ketersediaan: ${responseBody.ketersediaan}")
                                Toast.makeText(this@ActivityPesanKendaraan, "Ketersediaan: ${responseBody.ketersediaan}", Toast.LENGTH_SHORT).show()
                                binding.statusKendaraan.text = responseBody.ketersediaan.toString()
                            } else {
                                // Jika ketersediaan tidak tersedia, tampilkan pesan error
                                Log.e("CekKetersediaan", "Error: ${responseBody.success}")
                                Toast.makeText(this@ActivityPesanKendaraan, "Error: ${responseBody.success}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e("CekKetersediaan", "Response body is null")
                            Toast.makeText(this@ActivityPesanKendaraan, "Response body is null", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Jika request gagal
                        Log.e("CekKetersediaan", "Request gagal dengan status: ${response.code()}")
                        Toast.makeText(this@ActivityPesanKendaraan, "Request gagal dengan status: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Menangani exception yang terjadi
                withContext(Dispatchers.Main) {
                    Log.e("CekKetersediaan", "Error: ${e.message}")
                    Toast.makeText(this@ActivityPesanKendaraan, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}