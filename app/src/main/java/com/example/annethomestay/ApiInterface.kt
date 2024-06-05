package com.example.annethomestay

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("penginapan")
    fun getPenginapan(): Call<List<DataListPenginapan>>

    @GET("kendaraan")
    fun getKendaraan(): Call<List<DataListKendaraan>>

    @GET("objek")
    fun getRekomendasi(): Call<List<DataListRekomendasi>>

    @GET("promo")
    fun getPromo(): Call<List<DataListHome>>

    @GET("metode_bayar")
    fun getMetodeBayar(): Call<List<MetodeBayar>>
}
