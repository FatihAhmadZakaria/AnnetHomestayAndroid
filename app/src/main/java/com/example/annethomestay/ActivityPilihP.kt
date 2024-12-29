package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.annethomestay.databinding.ActivityPilihPBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityPilihP : AppCompatActivity() {
    private lateinit var binding: ActivityPilihPBinding
    private lateinit var penginapanArrayList: ArrayList<DataListPenginapan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihPBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigasi kembali
        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        // Memanggil API untuk mengambil data penginapan
        fetchPenginapanData()
    }

    private fun fetchPenginapanData() {
        val apiService = ApiClient.apiService
        val call = apiService.getPenginapan()

        call.enqueue(object : Callback<List<Penginapan>> {
            override fun onResponse(
                call: Call<List<Penginapan>>,
                response: Response<List<Penginapan>>
            ) {
                if (response.isSuccessful) {
                    val penginapanList = response.body() ?: emptyList()
                    penginapanArrayList = ArrayList()
                    Log.d("PromoResponse", "Response JSON: ${response.body()}")

                    for (penginapan in penginapanList) {
                        val imageUrls = penginapan.img.map { "https://annet.nosveratu.com/storage/app/public/${it.img_path}" } // List gambar
                        val fullImageUrlList = ArrayList(imageUrls)

                        val dataListPenginapan = DataListPenginapan(
                            id = penginapan.id,
                            img = fullImageUrlList,
                            nama = penginapan.nama,
                            deskrip = penginapan.deskrip ?: "",
                            fitur = penginapan.fitur,
                            kapasitas = penginapan.kapasitas,
                            harga = penginapan.harga
                        )

                        penginapanArrayList.add(dataListPenginapan)
                    }

                    setupListViewAdapter()
                } else {
                    Log.d("gagal", "Panggilan API gagal: ${response.message()}")
                }

            }

            override fun onFailure(call: Call<List<Penginapan>>, t: Throwable) {
                Log.d("gagal", "Panggilan API gagal: ${t.message}")
            }
        })
    }

    private fun setupListViewAdapter() {
        binding.listPenginapan.isClickable = true
        val adapter = DataListPenginapanAdapter(this, penginapanArrayList)
        binding.listPenginapan.adapter = adapter

        binding.listPenginapan.setOnItemClickListener { _, _, position, _ ->
            val data = penginapanArrayList[position]
            val intent = Intent(this, ActivityPesanPenginapan::class.java).apply {
                putExtra("id", data.id)
                putStringArrayListExtra("img", data.img) // Mengirim list gambar
                putExtra("nama", data.nama)
                putExtra("deskrip", data.deskrip)
                putExtra("fitur", data.fitur)
                putExtra("kapasitas", data.kapasitas)
                putExtra("harga", data.harga)
            }
            startActivity(intent)
        }
    }
}
