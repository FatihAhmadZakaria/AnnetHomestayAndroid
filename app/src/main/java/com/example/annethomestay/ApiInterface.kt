package com.example.annethomestay

import AuthResponse
import LoginRequest
import RegisterRequest
import UserDetailsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.Response

interface ApiInterface {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    @GET("user/details")
    fun getUserDetails(@Header("Authorization") token: String): Call<UserDetailsResponse>

    @POST("payment") // Ganti dengan endpoint yang sesuai di Laravel
    fun createTransaction(@Body request: TransactionRequest): Call<MidtransResponse>
}

