package com.example.annethomestay

import android.app.DatePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
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

class ActivityPesanPenginapan : AppCompatActivity() {
    private lateinit var gestureDetector: GestureDetector
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

        val name = intent.getStringExtra("name")
        val deskrip = intent.getStringExtra("deskrip")
        val kapasitas = intent.getStringExtra("kapasitas")
        val harga = intent.getIntExtra("harga", 0)

        binding.pengNama.text = name
        binding.pengDeskrip.text = deskrip
        binding.pengKapasitas.text = kapasitas
        binding.pengHarga.text = harga.toString()

        setupSpinners()
        setupPaymentSpinner()
        setupClipboard()
        initializeFlipperFromIntent()
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

    private fun setupSpinners() {
        val numberOfBookingsOptions = arrayOf("1", "2", "3", "4", "5")

        val numberOfBookingsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numberOfBookingsOptions)
        numberOfBookingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.pengJml.adapter = numberOfBookingsAdapter
        binding.pengDurasi.adapter = numberOfBookingsAdapter

        binding.pengJml.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = numberOfBookingsOptions[position]
                showToast("Jumlah Pemesanan: $selectedItem")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        binding.pengDurasi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = numberOfBookingsOptions[position]
                showToast("Durasi Hari: $selectedItem")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.pengTgl.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }
}
