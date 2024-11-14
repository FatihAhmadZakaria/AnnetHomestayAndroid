package com.example.annethomestay

import AuthResponse
import LoginRequest
import RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    @POST("payment") // Ganti dengan endpoint yang sesuai di Laravel
    fun createTransaction(@Body request: TransactionRequest): Call<MidtransResponse>

    @GET("promo")
    fun getPromo(): Call<List<Promo>>

    @GET("properti")
    fun getPenginapan(): Call<List<Penginapan>>

    @GET("kendaraan")
    fun getKendaraan(): Call<List<Kendaraan>>
}

