package com.example.annethomestay.utils

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.annethomestay.ApiClient
import com.example.annethomestay.DataListHome
import com.example.annethomestay.Promo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    val promoLiveData = MutableLiveData<List<DataListHome>>()

    fun fetchPromo() {
        val apiService = ApiClient.apiService
        val call = apiService.getPromo()

        call.enqueue(object : Callback<List<Promo>> {
            override fun onResponse(call: Call<List<Promo>>, response: Response<List<Promo>>) {
                if (response.isSuccessful) {
                    val promoList = response.body() ?: emptyList()
                    val dataListPromo = promoList.map { promo ->
                        val imageUrls = promo.img.map {
                            "https://annet.nosveratu.com/storage/app/public/${it.img_path}"
                        }
                        DataListHome(img = ArrayList(imageUrls), nama = promo.nama, deskrip = promo.deskripsi)
                    }
                    promoLiveData.value = dataListPromo
                } else {
                    Log.d("HomeViewModel", "API Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Promo>>, t: Throwable) {
                Log.d("HomeViewModel", "Failed to fetch promo: ${t.message}")
            }
        })
    }
}
