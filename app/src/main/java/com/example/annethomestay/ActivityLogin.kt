package com.example.annethomestay

import AuthResponse
import LoginRequest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityLoginBinding
import com.example.annethomestay.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ActivityLogin : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)

        // Misalnya, saat tombol login diklik
        findViewById<Button>(R.id.bt_login).setOnClickListener {
            val email = findViewById<EditText>(R.id.et_log_email).text.toString()
            val password = findViewById<EditText>(R.id.et_log_password).text.toString()
            loginUser(email, password)
        }
    }

    // Pastikan untuk menggunakan CoroutineScope dan handle response dengan benar.
    private fun loginUser(email: String, password: String) {
        val apiService = ApiClient.apiService
        val loginRequest = LoginRequest(email, password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<AuthResponse> = apiService.login(loginRequest).execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val authResponse = response.body()

                        // Menyimpan access_token ke session
                        sessionManager.saveToken(authResponse?.access_token ?: "")

                        val userDetailsResponse = apiService.getUserDetails("Bearer ${authResponse?.access_token}").execute()

                        if (userDetailsResponse.isSuccessful && userDetailsResponse.body() != null) {
                            val user = userDetailsResponse.body()

                            // Menyimpan id_user ke session
                            sessionManager.saveUser(user?.id_user ?: 0)

                            // Menampilkan nama depan sebagai toast
                            Toast.makeText(this@ActivityLogin, "Welcome, ${user?.nama_depan}", Toast.LENGTH_SHORT).show()

                            // Berpindah ke Activity utama
                            val intent = Intent(this@ActivityLogin, ActivityMain::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@ActivityLogin, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ActivityLogin, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ActivityLogin, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
