package com.example.annethomestay


import RegisterRequest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityRegister : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btRegister.setOnClickListener {
            val email = binding.registerEmail.text.toString()
            val namaDepan = binding.namaDepan.text.toString()
            val namaBelakang = binding.namaBelakang.text.toString()
            val password = binding.registerPassword.text.toString()
            val phone = binding.registerPhone.text.toString()

            if (email.isNotEmpty() && namaDepan.isNotEmpty() && namaBelakang.isNotEmpty() && password.isNotEmpty() && phone.isNotEmpty() ) {
                registerUser(email, namaDepan, namaBelakang, password, phone)
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
            Log.d("Cek InputF", namaDepan)
            Log.d("Cek InputR", namaBelakang)
            Log.d("Cek InputE", email)
            Log.d("Cek InputPA", password)
            Log.d("Cek InputPH", phone)
        }



        binding.toLogin.setOnClickListener {
            val i = Intent(this, ActivityLogin::class.java)
            startActivity(i)
        }
    }

    private fun registerUser(email: String, namaDepan: String, namaBelakang: String, password: String, phone: String) {
        val apiService = ApiClient.apiService
        val registerRequest = RegisterRequest(
            email = email,
            nama_depan = namaDepan,
            nama_belakang = namaBelakang,
            password = password,
            phone = phone
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.register(registerRequest).execute()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ActivityRegister,
                            "Registration successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@ActivityRegister, ActivityLogin::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@ActivityRegister,
                            "Registration failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ActivityRegister,
                        "An error occurred: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Register Request", "Nama Depan: $namaDepan, Nama Belakang: $namaBelakang, Phone: $phone, Email: $email, Password: $password")
                }
            }
        }
    }
}
