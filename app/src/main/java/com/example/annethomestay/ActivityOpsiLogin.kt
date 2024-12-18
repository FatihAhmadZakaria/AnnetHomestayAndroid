package com.example.annethomestay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
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

        val anim = AnimationUtils.loadAnimation(this@ActivityOpsiLogin, R.anim.bt_anim)

        binding.btOl1.setOnClickListener {
            it.startAnimation(anim)
            val i = Intent(this, ActivityLogin::class.java)
            startActivity(i)
        }

        binding.btOl2.setOnClickListener {
            it.startAnimation(anim)
            val i = Intent(this, ActivityRegister::class.java)
            startActivity(i)
        }

        binding.btOl3.setOnClickListener {
            it.startAnimation(anim)
            val i = Intent(this, ActivityTrialMidtrans::class.java)
            startActivity(i)
//            val redirectUrl = "https://app.sandbox.midtrans.com/snap/v4/redirection/ad5adc79-7913-46bb-85f8-873d63da4f8d" // Ganti dengan URL yang kamu terima
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
//            startActivity(intent)
        }

    }
}