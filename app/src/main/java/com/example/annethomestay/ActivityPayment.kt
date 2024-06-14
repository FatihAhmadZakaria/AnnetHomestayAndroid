package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityPaymentBinding

class ActivityPayment : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nama = intent.getStringExtra("nama")
        binding.pesanNama.text = nama

        binding.backBeranda.setOnClickListener {
            val i = Intent(this, ActivityMain::class.java)
            i.putExtra("open_fragment", "home")
            startActivity(i)
            finish()
        }

        binding.pesanBatal.setOnClickListener {
            val i =Intent(this, ActivityBatalPesan::class.java)
            startActivity(i)
            finish()
        }
    }
}