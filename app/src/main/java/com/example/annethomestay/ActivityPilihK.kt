package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityPilihKBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityPilihK : AppCompatActivity() {
    private lateinit var binding: ActivityPilihKBinding
    private lateinit var kenArrayList: ArrayList<DataListKendaraan>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihKBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val apiService = ApiClient.apiService
        val call = apiService.getKendaraan()
        call.enqueue(object : Callback<List<DataListKendaraan>> {
            override fun onResponse(
                call: Call<List<DataListKendaraan>>,
                response: Response<List<DataListKendaraan>>
            ) {
                if (response.isSuccessful) {
                    val dataListkendaraan = response.body() ?: emptyList()
                    kenArrayList = ArrayList()

                    for (kendaraan in dataListkendaraan){
                        val dataListKendaraan = DataListKendaraan(
                            img = kendaraan.img,
                            nama = kendaraan.nama,
                            harga = kendaraan.harga,
                            status = kendaraan.status,
                            id = kendaraan.id
                        )
                        kenArrayList.add(dataListKendaraan)
                    }

                    binding.listKendaraan.isClickable = true
                    binding.listKendaraan.adapter = DataListKendaraanAdapter(this@ActivityPilihK, kenArrayList)
                    binding.listKendaraan.setOnItemClickListener { parent, view, position, id ->
                        val data = kenArrayList[position]

                        val i = Intent(this@ActivityPilihK, ActivityPesanKendaraan::class.java)
                        i.putExtra("img", data.img)
                        i.putExtra("nama", data.nama)
                        i.putExtra("harga", data.harga)
                        i.putExtra("status", data.status)
                        i.putExtra("id", data.id)
                        startActivity(i)
                    }
                } else {
                    Log.d("gagal", "Panggilan API gagal: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<DataListKendaraan>>, t: Throwable) {
                Log.d("gagal", "Panggilan API gagal: ${t.message}")
            }
        })

        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }
}