package com.example.annethomestay

import DataListPenginapanAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityPilihPBinding
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback

class ActivityPilihP : AppCompatActivity() {
    private lateinit var binding: ActivityPilihPBinding
    private lateinit var penginapanArrayList: ArrayList<DataListPenginapan>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPilihPBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.icBack.setOnClickListener {
            onBackPressed()
        }

        val apiService = ApiClient.apiService
//        val call = apiService.getPenginapan()
//        call.enqueue(object : Callback<List<DataListPenginapan>> {
//            override fun onResponse(
//                call: Call<List<DataListPenginapan>>,
//                response: retrofit2.Response<List<DataListPenginapan>>
//            ) {
//                if (response.isSuccessful) {
//                    val dataListPenginapan = response.body() ?: emptyList()
//                    penginapanArrayList = ArrayList()
//
//                    for (penginapan in dataListPenginapan) {
//                        val dataListPenginapan = DataListPenginapan(
//                            id = penginapan.id,
//                            img = penginapan.img,
//                            imgFlip = penginapan.imgFlip,
//                            name = penginapan.name,
//                            deskrip = penginapan.deskrip,
//                            kapasitas = penginapan.kapasitas,
//                            harga = penginapan.harga,
//                        )
//                        penginapanArrayList.add(dataListPenginapan)
//                    }
//
//                    // Set adapter untuk ListView
//                    binding.listPenginapan.isClickable = true
//                    val adapter = DataListPenginapanAdapter(this@ActivityPilihP, penginapanArrayList)
//                    binding.listPenginapan.adapter = adapter
//                    binding.listPenginapan.setOnItemClickListener { parent, view, position, id ->
//                        val data = penginapanArrayList[position]
//
//                        val i = Intent(this@ActivityPilihP, ActivityPesanPenginapan::class.java)
//                        i.putExtra("id", data.id)
//                        i.putExtra("img", data.img)
//                        i.putExtra("imgSpin", data.imgFlip)
//                        i.putExtra("name", data.name)
//                        i.putExtra("deskrip", data.deskrip)
//                        i.putExtra("kapasitas", data.kapasitas)
//                        i.putExtra("harga", data.harga)
//                        startActivity(i)
//                    }
//                } else {
//                    Log.d("gagal", "Panggilan API gagal: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<DataListPenginapan>>, t: Throwable) {
//                Log.d("gagal", "Panggilan API gagal: ${t.message}")
//            }
//
//        })
    }
}