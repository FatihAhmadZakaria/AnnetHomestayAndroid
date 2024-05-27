package com.example.annethomestay

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.annethomestay.databinding.FragmentProfileBinding

class FragmentProfile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
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
        binding.exit.setOnClickListener {
            activity?.finish()
        }
        binding.profileSecurity.setOnClickListener {
            showChangePasswordDialog()
        }
        binding.profilePhone.setOnClickListener {
            showChangePhoneDialog()
        }
    }

    @SuppressLint("InflateParams", "MissingInflatedId")
    private fun showChangePhoneDialog() {
        val dialogView = layoutInflater.inflate(R.layout.popup_phone, null)
        val currentPhone = dialogView.findViewById<EditText>(R.id.et_ph_old)
        val newPhone = dialogView.findViewById<EditText>(R.id.et_ph_new)

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Ganti Nomor")
            .setView(dialogView)
            .setPositiveButton("Ganti") { dialog, _ ->
                val currentPhone = currentPhone.text.toString()
                val newPhone = newPhone.text.toString()
                changePhone(currentPhone, newPhone)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
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

                // Lakukan validasi dan logika untuk mengganti password
                if (newPass == confirmPass) {
                    // Panggil fungsi untuk mengganti password di sini
                    changePassword(currentPass, newPass)
                    dialog.dismiss()
                } else {
                    // Tampilkan pesan kesalahan jika password tidak cocok
                    Toast.makeText(requireContext(), "Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show()
                    // Jangan dismiss dialog di sini
                }
            }

            negativeButton.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }


    private fun changePassword(currentPass: String, newPass: String) {
        // Implementasikan logika untuk mengganti password di sini
        // Misalnya, Anda dapat memanggil API atau memperbarui database
        Toast.makeText(requireContext(), "Sukses ganti password", Toast.LENGTH_SHORT).show()
    }

    private fun changePhone(currentPhone: String, newPhone: String) {
        Toast.makeText(requireContext(), "Sukses ganti nomor", Toast.LENGTH_SHORT).show()
    }
}