package com.example.annethomestay

import AuthResponse
import LoginRequest
import LogoutResponse
import RegisterRequest
import TransaksiResponse
import TransaksiResponsePenginapan
import retrofit2.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("logout")
    suspend fun logout(): Response<LogoutResponse>

    @POST("transaksi_kendaraan")
    fun transaksiKendaraan(@Body transaction: TransaksiKendaraan): Call<TransaksiResponse>

    @POST("transaksi_penginapan")
    fun transaksiPenginapan(@Body transaction: TransaksiPenginapan): Call<TransaksiResponsePenginapan>

    @GET("riwayat/{id}")
    fun getRiwayat(@Path("id") id: Int): Call<List<DataListTransaksi>>

    @GET("user/{id}")
    suspend fun getUser(@Path("id") id: Int): Response<DataUser>
}
