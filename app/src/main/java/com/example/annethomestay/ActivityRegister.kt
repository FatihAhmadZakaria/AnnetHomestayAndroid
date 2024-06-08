package com.example.annethomestay


import RegisterRequest
import android.content.Intent
import android.os.Bundle
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
            val name = binding.etRegUname.text.toString()
            val email = binding.etRegEmail.text.toString()
            val password = binding.etRegPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(name, email, password)
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

        binding.toLogin.setOnClickListener {
            val i = Intent(this, ActivityLogin::class.java)
            startActivity(i)
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        val apiService = ApiClient.apiService
        val registerRequest = RegisterRequest(name, email, password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.register(registerRequest)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Registration successful
                        Toast.makeText(this@ActivityRegister, "Registration successful", Toast.LENGTH_SHORT).show()
                        val i = Intent(this@ActivityRegister, ActivityLogin::class.java)
                        startActivity(i)
                    } else {
                        // Registration failed
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Toast.makeText(this@ActivityRegister, "Registration failed: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // An error occurred
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ActivityRegister, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
