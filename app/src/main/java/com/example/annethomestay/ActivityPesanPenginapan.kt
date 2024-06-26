package com.example.annethomestay

import TransaksiResponsePenginapan
import android.app.DatePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityPesanPenginapanBinding
import com.example.annethomestay.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class ActivityPesanPenginapan : AppCompatActivity() {
    private lateinit var binding: ActivityPesanPenginapanBinding
    private var jumlahKamar: Int = 1
    private val paymentMethods = mutableListOf<String>()
    private val paymentDetails = mutableListOf<String>()
    private var selectedPaymentMethodId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPesanPenginapanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name = intent.getStringExtra("name")
        val deskrip = intent.getStringExtra("deskrip")
        val kapasitas = intent.getStringExtra("kapasitas")
        val harga = intent.getIntExtra("harga", 0)
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        val formattedHarga = formatRupiah.format(harga.toLong())

        binding.pengNama.text = name
        binding.pengDeskrip.text = deskrip
        binding.pengKapasitas.text = kapasitas
        binding.pengHarga.text = formattedHarga
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
        binding.pengTglIn.setOnClickListener {
            showDatePickerDialogIn()
        }
        binding.pengTglOut.setOnClickListener {
            showDatePickerDialogOut()
        }

        setupPaymentSpinner()
        setupClipboard()
        initializeFlipperFromIntent()

        binding.btPesanPeng.setOnClickListener {
            val paymentMethodId = selectedPaymentMethodId
            pesanPenginapan(paymentMethodId)
        }

        fetchPaymentMethods()

        binding.plus.setOnClickListener {
            increaseRoomCount()
        }

        binding.min.setOnClickListener {
            decreaseRoomCount()
        }

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


    private fun increaseRoomCount() {
        jumlahKamar++
        updateRoomCount()
    }

    private fun decreaseRoomCount() {
        if (jumlahKamar > 0) {
            jumlahKamar--
            updateRoomCount()
        }
    }

    private fun hitungSelisihHari(tglIn: String, tglOut: String): Int {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateIn = dateFormat.parse(tglIn)
        val dateOut = dateFormat.parse(tglOut)

        val selisih = dateOut.time - dateIn.time
        return TimeUnit.DAYS.convert(selisih, TimeUnit.MILLISECONDS).toInt()
    }

    private fun updateRoomCount() {
        val tglIn = binding.pengTglIn.text.toString()
        val tglOut = binding.pengTglOut.text.toString()

        if (tglIn.isNotEmpty() && tglOut.isNotEmpty()) {
            val hargaPerMalam = intent.getIntExtra("harga", 0) // Harga per malam dari TextView
            val selisihHari = hitungSelisihHari(tglIn, tglOut)
            val totalBayarDP = hargaPerMalam * selisihHari * jumlahKamar

            val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            val formattedPrice = formatter.format(totalBayarDP.toDouble())

            binding.pengTotal.text = formattedPrice
        } else {
            showToast("Silakan pilih tanggal check-in dan check-out")
        }
        binding.durasi.text = jumlahKamar.toString()
    }

    private fun initializeFlipperFromIntent() {
        val imgSpin = intent.getStringExtra("imgSpin")
        if (imgSpin != null) {
            val resId = resources.getIdentifier(imgSpin, "array", packageName)
            if (resId != 0) {
                val images = resources.getStringArray(resId)
                setupViewFlipper(images)
            } else {
                // Handle error: resource not found
                showToast("Resource array tidak ditemukan: $imgSpin")
            }
        } else {
            // Handle error: imgSpin is null
            showToast("Intent tidak mengandung imgSpin")
        }
    }

    private fun setupViewFlipper(images: Array<String>) {
        val viewFlipper = binding.slider
        viewFlipper.inAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        viewFlipper.outAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)

        for (image in images) {
            val imageView = ImageView(this)
            val imageResId = resources.getIdentifier(image, "drawable", packageName)
            if (imageResId != 0) {
                imageView.setImageResource(imageResId)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                viewFlipper.addView(imageView)
            } else {
                // Handle error: image resource not found
                showToast("Resource gambar tidak ditemukan: $image")
            }
        }

        // Mulai otomatis ViewFlipper
        viewFlipper.isAutoStart = true
        viewFlipper.flipInterval = 3000 // Interval waktu dalam milidetik
        viewFlipper.startFlipping()
    }

    private fun setupPaymentSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.pengBayar.adapter = adapter
    }

    private fun setupClipboard() {
        binding.copyIcon.setOnClickListener {
            copyToClipboard("1234567890")
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("No Rekening", text)
        clipboard.setPrimaryClip(clip)
        showToast("No Rekening disalin ke clipboard")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDatePickerDialogIn() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.pengTglIn.setText(selectedDate)
            updateRoomCount()
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showDatePickerDialogOut() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.pengTglOut.setText(selectedDate)
            updateRoomCount()
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun pesanPenginapan(paymentMethodId: Long) {
        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId()
        val penginapanId = intent.getIntExtra("id", 0)

        val tglIn = binding.pengTglIn.text.toString()
        val tglOut = binding.pengTglOut.text.toString()
        val durasi = hitungSelisihHari(tglIn, tglOut)
        val namaInap = binding.pengNama.text.toString()

        val data = TransaksiPenginapan(
            id_p = penginapanId,
            id_mtd = paymentMethodId,
            user_id = userId, // Pastikan ini benar
            tgl_in = formatDateString(tglIn, "dd/MM/yyyy", "yyyy-MM-dd"),
            tgl_out = formatDateString(tglOut, "dd/MM/yyyy", "yyyy-MM-dd"),
            durasi_trans_p = durasi,
            stat_trans_p = "pending",
            total_bayar = 500000,
        )

        val apiService = ApiClient.apiService
        apiService.transaksiPenginapan(data).enqueue(object : Callback<TransaksiResponsePenginapan> {
            override fun onResponse(
                call: Call<TransaksiResponsePenginapan>,
                response: Response<TransaksiResponsePenginapan>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        val intent = Intent(this@ActivityPesanPenginapan, ActivityPayment::class.java)
                        intent.putExtra("nama", namaInap) // Mengirim nama penginapan
                        startActivity(intent)
                        finish()
                    } else {
                        Log.d("API_ERROR", "Response body is null")
                        Toast.makeText(this@ActivityPesanPenginapan, "Gagal memesan penginapan: Response body is null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("API_ERROR", "Response error: $errorBody")
                    Toast.makeText(this@ActivityPesanPenginapan, "Gagal memesan penginapan: ${response.message()} ${paymentMethodId}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TransaksiResponsePenginapan>, t: Throwable) {
                Log.d("API_FAILURE", "Failure message: ${t.message}")
                Toast.makeText(this@ActivityPesanPenginapan, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
                        this@ActivityPesanPenginapan,
                        "Failed to fetch payment methods",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<MetodeBayar>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@ActivityPesanPenginapan,
                    "Failed to fetch payment methods",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun formatDateString(dateString: String, fromFormat: String, toFormat: String): String {
        val fromDateFormat = SimpleDateFormat(fromFormat, Locale.getDefault())
        val toDateFormat = SimpleDateFormat(toFormat, Locale.getDefault())
        val date = fromDateFormat.parse(dateString)
        return toDateFormat.format(date)
    }
}