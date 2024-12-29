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
import androidx.core.content.ContextCompat
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
import java.time.ZoneId
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
            .withMerchantUrl("https://annet.nosveratu.com/api/midtrans/callback/") // Ganti dengan URL server untuk menangani callback
            .enableLog(true) // Aktifkan log SDK
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // Tema warna
            .build()

        setLocaleNew("id")

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                    Log.d("MIDTRANS", "Transaction ID: ${transactionResult?.transactionId}")
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

                // Atur tanggal checkout otomatis menjadi 1 hari setelah tanggal check-in
                checkoutDate = checkinDate?.plusDays(1)
                val autoCheckoutDate = checkoutDate?.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                tglCheckout.text = autoCheckoutDate ?: "Tanggal tidak valid"
                tglCheckout.setTextColor(Color.BLACK)

                // Simpan tanggal-tanggal ke SessionManager
                SessionManager(this@ActivityPesanKendaraan).saveTglMulai(selectedDate)
                SessionManager(this@ActivityPesanKendaraan).saveTglSelesai(autoCheckoutDate.orEmpty())

                // Hitung jumlah hari
                updateHarga()
            }
        }

        tglCheckout.setOnClickListener {
            val localCheckinDate = checkinDate // Salin ke variabel lokal untuk mencegah perubahan di antara pemeriksaan
            if (localCheckinDate != null) {
                showDatePickerDialog(minDate = localCheckinDate.plusDays(1)) { selectedDate ->
                    tglCheckout.text = selectedDate
                    tglCheckout.setTextColor(Color.BLACK)

                    // Simpan tanggal check-out ke variabel
                    checkoutDate = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))

                    // Simpan tanggal checkout ke SessionManager
                    SessionManager(this@ActivityPesanKendaraan).saveTglSelesai(selectedDate)

                    // Hitung jumlah hari
                    updateHarga()
                    Log.d("SELECT_DATE", "Select Date: ${selectedDate}")
                }
            } else {
                Toast.makeText(this@ActivityPesanKendaraan, "Pilih tanggal check-in terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btPesanKen.setOnClickListener {
            updateHarga()
//            val jumlahPesan = jumlahSewa
            val reservasiRequest = ReservasiRequest(
                id_user = idUser,
                id_produk = idProduk,
                tipe_produk = tipeProduk,
                tgl_mulai = tglCheckin.text.toString(),
                tgl_selesai = tglCheckout.text.toString(),
                jumlah_pesan = 1
            )

            // Mengirimkan request menggunakan Retrofit
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response: Response<ReservasiResponse> = apiService.createReservasi(reservasiRequest)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            Log.d("API Response", "Response: $responseBody")
                            Log.d("API Response", "Response Code: ${response.code()}")
                            if (responseBody?.success == true) {
                                Log.d("RESERVASI", "Reservasi berhasil")
                                SessionManager(this@ActivityPesanKendaraan).saveReservasiId(responseBody.id_reservasi)
                                val reservasiId = SessionManager(this@ActivityPesanKendaraan).getReservasiId()

                                Log.d("Reservasi ID", "ID Reservasi yang disimpan: $reservasiId")
                                bayarReservasi()
                            } else {
                                Log.d("RESERVASI", "Gagal melakukan reservasi")
                            }
                        } else {
                            Log.e("API Error", "Response Code: ${response.code()} |Response Error ${response.message()}")
                        }

                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("RESERVASI", "Error: ${e.message}")
                    }
                }
            }
        }
    }

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    // Fungsi pilih tanggal dengan opsi tanggal minimum
    @SuppressLint("DefaultLocale")
    private fun showDatePickerDialog(minDate: LocalDate? = null, onDateSelected: (String) -> Unit) {
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
        datePickerDialog.datePicker.minDate = minDate?.atStartOfDay(ZoneId.systemDefault())?.toEpochSecond()?.times(1000)
            ?: System.currentTimeMillis()

        // Tampilkan DatePickerDialog
        datePickerDialog.show()
    }

    private fun updateHarga() {
        // Mengecek apakah checkinDate dan checkoutDate tidak null
        val daysBetween = if (checkinDate != null && checkoutDate != null) {
            // Menghitung jumlah hari jika kedua tanggal ada
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ChronoUnit.DAYS.between(checkinDate, checkoutDate).toInt()
            } else {
                1
            }
        } else {
            1
        }

        if (daysBetween < 0) {
            Log.d("UPDATE_HARGA", "Tanggal check-out harus setelah check-in!")
        } else {
            Log.d("UPDATE_HARGA", "Jumlah hari menginap: $daysBetween hari")
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
            Log.d("BAYAR", "ID Reservasi tidak ditemukan")
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
                            Toast.makeText(this@ActivityPesanKendaraan, "Pembayaran tercatat", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.d("BAYAR", "Error: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("BAYAR", "Error: ${e.message}")
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
                                val ketersediaan = responseBody.ketersediaan
                                Log.d("CekKetersediaan", "Ketersediaan: ${responseBody.ketersediaan}")
                                binding.statusKendaraan.text = responseBody.ketersediaan.toString()
                                // Validasi ketersediaan
                                if (ketersediaan == "Tidak Tersedia") {
                                    binding.btPesanKen.text = "Tidak Tersedia"
                                    binding.btPesanKen.isEnabled = false
                                    binding.btPesanKen.setBackgroundColor(
                                        ContextCompat.getColor(this@ActivityPesanKendaraan, R.color.grey)
                                    )
                                } else {
                                    binding.btPesanKen.text = "Pesan"
                                    binding.btPesanKen.isEnabled = true
                                    binding.btPesanKen.setBackgroundResource(R.drawable.bt_rounded)
                                }
                            } else {
                                Log.e("CekKetersediaan", "Error: ${responseBody.success}")
                            }
                        } else {
                            Log.e("CekKetersediaan", "Response body is null")
                        }
                    } else {
                        Log.e("CekKetersediaan", "Request gagal dengan status: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("CekKetersediaan", "Error: ${e.message}")
                }
            }
        }
    }
}