package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val apiService = ApiClient.apiService
//        val call = apiService.getRekomendasi()
//        call.enqueue(object : Callback<List<DataListRekomendasi>> {
//            override fun onResponse(
//                call: Call<List<DataListRekomendasi>>,
//                response: Response<List<DataListRekomendasi>>
//            ) {
//                if (response.isSuccessful) {
//                    val dataListRekomendasi = response.body() ?: emptyList()
//                    objekArrayList = ArrayList()
//
//                    for (objek in dataListRekomendasi) {
//                        val dataListRekomendasi = DataListRekomendasi(
//                            imgObj = objek.imgObj,
//                            nameObj = objek.nameObj,
//                            deskrip = objek.deskrip,
//                            linkObj = objek.linkObj
//                        )
//                        objekArrayList.add(dataListRekomendasi)
//                    }
//
//                    binding.listRekObj.isClickable = true
//                    val adapter = DataListRekomendasiAdapter(this@ActivityPilihO, objekArrayList)
//                    binding.listRekObj.adapter = adapter
//                    binding.listRekObj.setOnItemClickListener { parent, view, position, id ->
//                        val data = objekArrayList[position]
//
//                        val i = Intent(this@ActivityPilihO, ActivityDetailObjek::class.java)
//                        i.putExtra("img", data.imgObj)
//                        i.putExtra("nama", data.nameObj)
//                        i.putExtra("deskrip", data.deskrip)
//                        i.putExtra("link", data.linkObj)
//                        startActivity(i)
//                    }
//                } else {
//                    Log.d("gagal", "Panggilan API gagal: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<DataListRekomendasi>>, t: Throwable) {
//                Log.d("gagal", "Panggilan API gagal: ${t.message}")
//            }
//        })
//
//        binding.icBackObjek.setOnClickListener {
//            onBackPressed()
//        }
    }
}