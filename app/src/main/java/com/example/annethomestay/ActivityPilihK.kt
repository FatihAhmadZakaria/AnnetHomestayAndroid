package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
        setContentView(binding.root)

        // Navigasi kembali
        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        // Memanggil API untuk mengambil data kendaraan
        fetchKendaraanData()
    }

    private fun fetchKendaraanData() {
        val apiService = ApiClient.apiService
        val call = apiService.getKendaraan()

        call.enqueue(object : Callback<List<Kendaraan>> {
            override fun onResponse(
                call: Call<List<Kendaraan>>,
                response: Response<List<Kendaraan>>
            ) {
                if (response.isSuccessful) {
                    val kendaraanList = response.body() ?: emptyList()
                    kenArrayList = ArrayList()
                    Log.d("KendaraanResponse", "Response JSON: ${response.body()}")

                    for (kendaraan in kendaraanList) {
                        val imageUrls = kendaraan.img.map { "https://annet.nosveratu.com/storage/app/public/${it.img_path}" } // List gambar
                        val fullImageUrlList = ArrayList(imageUrls)

                        val dataListKendaraan = DataListKendaraan(
                            id = kendaraan.id,
                            img = fullImageUrlList,
                            nama = kendaraan.nama,
                            harga = kendaraan.harga,
                            deskrip = kendaraan.deskrip
                        )

                        kenArrayList.add(dataListKendaraan)
                    }

                    setupListViewAdapter()
                } else {
                    Log.d("gagal", "Panggilan API gagal: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Kendaraan>>, t: Throwable) {
                Log.d("gagal", "Panggilan API gagal: ${t.message}")
            }
        })
    }

    private fun setupListViewAdapter() {
        binding.listKendaraan.isClickable = true
        val adapter = DataListKendaraanAdapter(this, kenArrayList)
        binding.listKendaraan.adapter = adapter

        binding.listKendaraan.setOnItemClickListener { _, _, position, _ ->
            val data = kenArrayList[position]
            val intent = Intent(this, ActivityPesanKendaraan::class.java).apply {
                putExtra("id", data.id)
                putStringArrayListExtra("img", data.img) // Mengirim list gambar
                putExtra("nama", data.nama)
                putExtra("harga", data.harga)
                putExtra("deskrip", data.deskrip)
            }
            startActivity(intent)
        }
    }
}
