package com.example.annethomestay

import PasswordUpdateRequest
import PhoneUpdateRequest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.annethomestay.databinding.FragmentProfileBinding
import com.example.annethomestay.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentProfile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sessionManager: SessionManager
    private var userId: Int = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileAbout.setOnClickListener {
            val i = Intent(requireContext(),ActivityAbout::class.java)
            startActivity(i)
        }

        val nama = SessionManager(requireContext()).getNama()
        binding.profileName.text = nama
        val email = SessionManager(requireContext()).getEmail()
        binding.profileEmail.text = email

        binding.profileSecurity.setOnClickListener {
            showChangePasswordDialog()
        }
        binding.profilePhone.setOnClickListener {
            showChangePhoneDialog()
        }
        sessionManager = SessionManager(requireContext())
        userId = sessionManager.getUserId()

        // Panggil fungsi untuk mengambil dan menampilkan data pengguna

    }

    private fun showChangePhoneDialog() {
        val dialogView = layoutInflater.inflate(R.layout.popup_phone, null)
        val currentPhone = dialogView.findViewById<EditText>(R.id.et_ph_old)
        val newPhone = dialogView.findViewById<EditText>(R.id.et_ph_new)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Ganti Nomor")
            .setView(dialogView)
            .setPositiveButton("Ganti", null) // Biarkan null untuk menangani klik sendiri
            .setNegativeButton("Batal", null)

        val dialog = dialogBuilder.create() // Buat dialog dari dialogBuilder

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_red))
            negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_red))

            positiveButton.setOnClickListener {
                val currentPh = currentPhone.text.toString()
                val newPh = newPhone.text.toString()

                // Panggil fungsi untuk mengganti nomor telepon di sini
                changePhone(currentPh, newPh)
                dialog.dismiss()
            }

            negativeButton.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show() // Pastikan untuk memanggil show() agar dialog muncul
    }

    private fun showChangePasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.popup_security, null)
        val currentPassword = dialogView.findViewById<EditText>(R.id.et_sec_old)
        val newPassword = dialogView.findViewById<EditText>(R.id.et_sec_new)
        val confirmPassword = dialogView.findViewById<EditText>(R.id.et_sec_new_confirm)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Ganti Password")
            .setView(dialogView)
            .setPositiveButton("Ganti", null) // Biarkan null untuk menangani klik sendiri
            .setNegativeButton("Batal", null)

        val dialog = dialogBuilder.create() // Buat dialog dari dialogBuilder

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_red))
            negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_red))

            positiveButton.setOnClickListener {
                val currentPass = currentPassword.text.toString()
                val newPass = newPassword.text.toString()
                val confirmPass = confirmPassword.text.toString()

                if (newPass.isEmpty() || confirmPass.isEmpty() || currentPass.isEmpty()) {
                    Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (newPass != confirmPass) {
                    Toast.makeText(requireContext(), "Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                changePassword(currentPass, newPass)
                dialog.dismiss()
            }


            negativeButton.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun changePassword(currentPass: String, newPass: String) {
        val token = "Bearer ${sessionManager.getAccessToken()}" // Dapatkan token dari SessionManager
        val passwordRequest = PasswordUpdateRequest(sandi_lama = currentPass, sandi_baru = newPass)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ApiClient.apiService
                val response = apiService.updatePassword(token, passwordRequest)

                withContext(Dispatchers.Main) {
                    if (response.success) {
                        Toast.makeText(requireContext(), "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun changePhone(currentPhone: String, newPhone: String) {
        val token = "Bearer ${sessionManager.getAccessToken()}" // Dapatkan token dari SessionManager
        val phoneRequest = PhoneUpdateRequest(no_lama = currentPhone, no_baru = newPhone)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ApiClient.apiService
                val response = apiService.updatePhone(token, phoneRequest)

                withContext(Dispatchers.Main) {
                    if (response.success) {
                        Toast.makeText(requireContext(), "Nomor telepon berhasil diubah", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}