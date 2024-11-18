package com.example.annethomestay

import AuthResponse
import LoginRequest
import RegisterRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("/api/reservasi")
    suspend fun createReservasi(@Body reservasiRequest: ReservasiRequest): Response<ReservasiResponse>

    @POST("/api/reservasi/{id_reservasi}/bayar")
    suspend fun bayarReservasi(
        @Path("id_reservasi") id_reservasi: Int,
        @Body bayarRequest: BayarRequest
    ): Response<BayarResponse>

    @GET("riwayat/{id_user}")
    fun getRiwayat(@Path("id_user") idUser: Int): Call<RiwayatResponse>

    @GET("objek")
    fun getObjek(): Call<List<Objek>>

    @POST("cek-properti/{id_properti}")
    suspend fun cekKetersediaan(
        @Body request: CekKetersediaanRequest,
        @Path("id_properti") idProperti: Int
    ): Response<CekKetersediaanResponse>

    @POST("cek-kendaraan/{id_properti}")
    suspend fun cekKetersediaanKendaraan(
        @Body request: CekKetersediaanRequest,
        @Path("id_properti") idProperti: Int
    ): Response<CekKetersediaanResponseKen>
}

