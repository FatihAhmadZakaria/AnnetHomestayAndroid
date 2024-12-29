package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.annethomestay.databinding.ActivityPilihOBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityPilihO : AppCompatActivity() {
    private lateinit var binding: ActivityPilihOBinding
    private lateinit var objekArrayList: ArrayList<DataListRekomendasi>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihOBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Memanggil API untuk mengambil data objek wisata
        fetchObjekWisataData()
        binding.icBack.setOnClickListener {
            onBackPressed()
        }    }

    private fun fetchObjekWisataData() {
        val apiService = ApiClient.apiService
        val call = apiService.getObjek()

        call.enqueue(object : Callback<List<Objek>> {
            override fun onResponse(
                call: Call<List<Objek>>,
                response: Response<List<Objek>>
            ) {
                if (response.isSuccessful) {
                    val objekList = response.body() ?: emptyList()
                    objekArrayList = ArrayList()
                    Log.d("ObjekWisataResponse", "Response JSON: ${response.body()}")

                    for (objek in objekList) {
                        // Validasi jika img kosong
                        val imageUrls = if (!objek.img.isNullOrEmpty()) {
                            objek.img.map { "https://annet.nosveratu.com/storage/app/public/${it.img_path}" }
                        } else {
                            emptyList()
                        }
                        val fullImageUrlList = ArrayList(imageUrls)

                        val dataListRekomendasi = DataListRekomendasi(
                            img = fullImageUrlList,
                            nama = objek.nama,
                            deskripsi = objek.deskripsi,
                            link = objek.link
                        )

                        objekArrayList.add(dataListRekomendasi)
                    }

                    setupListViewAdapter()
                } else {
                    Log.d("gagal", "Panggilan API gagal: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Objek>>, t: Throwable) {
                Log.d("gagal", "Panggilan API gagal: ${t.message}")
            }
        })

    }

    private fun setupListViewAdapter() {
        binding.listRekObj.isClickable = true
        val adapter = DataListRekomendasiAdapter(this, objekArrayList)
        binding.listRekObj.adapter = adapter

        binding.listRekObj.setOnItemClickListener { _, _, position, _ ->
            val data = objekArrayList[position]
            val intent = Intent(this, ActivityDetailObjek::class.java).apply {
                putStringArrayListExtra("img", data.img) // Mengirim list gambar lengkap
                putExtra("nama", data.nama)
                putExtra("deskrip", data.deskripsi)
                putExtra("link", data.link)
            }
            startActivity(intent)
        }

    }

}
