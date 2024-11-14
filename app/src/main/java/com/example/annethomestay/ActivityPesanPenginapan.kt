package com.example.annethomestay

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.ViewFlipper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.annethomestay.databinding.ActivityPesanPenginapanBinding
import java.util.Calendar

private var durasi: Int = 1 // Inisialisasi durasi dengan nilai default

class ActivityPesanPenginapan : AppCompatActivity() {
    private lateinit var binding: ActivityPesanPenginapanBinding

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

        val id = intent.getIntExtra("id", 0) // Default 0 jika tidak ada
        val img = intent.getStringArrayListExtra("img") ?: arrayListOf() // Default empty list jika tidak ada
        val nama = intent.getStringExtra("nama") ?: "Nama tidak tersedia"
        val deskrip = intent.getStringExtra("deskrip") ?: "Deskripsi tidak tersedia"
        val fitur = intent.getStringExtra("fitur") ?: "Fitur tidak tersedia"
        val kapasitas = intent.getIntExtra("kapasitas", 0) // Default 0 jika tidak ada
        val harga = intent.getIntExtra("harga", 0) // Default 0 jika tidak ada

        binding.icBack.setOnClickListener {
            onBackPressed()
        }
        // Menampilkan data ke UI
        binding.pengNama.text = nama
        binding.pengHarga.text = "Harga: $harga"
        binding.pengDeskrip.text = deskrip
        binding.pengFitur.text = fitur
        binding.pengKapasitas.text = "Kapasitas: $kapasitas"

        val flipper: ViewFlipper = findViewById(R.id.slider)

        for (imgUrl in img) {
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

        // Plus minus jumlah
        durasiJumlahKamar()

        // Dropdown
        val dropDown = binding.dropdownMenu
        val itemsDropdown = listOf("Full", "DP")
        menuDropdown(dropDown, itemsDropdown)
        dropDown.setSelection(0)

        // Pilih tanggal
        val tglCheckin = binding.tglCheckin
        val tglCheckout = binding.tglCheckout
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

    // Fungsi menu dropdown pilihan pembayaran
    private fun menuDropdown(spinner: Spinner, items: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
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

    // Plus minus jumlah
    private fun durasiJumlahKamar() {
        val minusButton = binding.min
        val plusButton = binding.plus
        val textDurasi = binding.durasi
        // Set OnClickListener untuk tombol minus
        minusButton.setOnClickListener {
            if (durasi > 1) { // Pastikan durasi tidak kurang dari 1
                durasi--
                textDurasi.text = durasi.toString()
            }
        }

        // Set OnClickListener untuk tombol plus
        plusButton.setOnClickListener {
            durasi++
            textDurasi.text = durasi.toString()
        }
    }
}