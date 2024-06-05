package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityBatalPesanBinding

class ActivityBatalPesan : AppCompatActivity() {
    private lateinit var binding: ActivityBatalPesanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBatalPesanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val itemSpinner = arrayOf("Salah pesan", "Batal karena jadwal", "Alasan lain")

        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemSpinner)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBatal.adapter = adapterSpinner
        binding.spinnerBatal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nothing
            }
        }

        binding.pesanBatal.setOnClickListener {
            val i = Intent(this, ActivityMain::class.java)
            i.putExtra("open_fragment", "home")
            startActivity(i)
        }
        binding.backBeranda.setOnClickListener {
            val i = Intent(this, ActivityMain::class.java)
            i.putExtra("open_fragment", "home")
            startActivity(i)
        }
    }
}