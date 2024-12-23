package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityOpsiLoginBinding

class ActivityOpsiLogin : AppCompatActivity() {
    private lateinit var binding: ActivityOpsiLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOpsiLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btOl1.setOnClickListener {

            val i = Intent(this, ActivityLogin::class.java)
            startActivity(i)
        }
        binding.btOl2.setOnClickListener {

            val i = Intent(this, ActivityRegister::class.java)
            startActivity(i)
        }
        binding.btOl3.setOnClickListener {
            val i = Intent(this, ActivityTrialMidtrans::class.java)
            startActivity(i)
        }

    }
}