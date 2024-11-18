package com.example.annethomestay

data class ReservasiRequest(
    val id_user: Int,
    val id_produk: Int,
    val tipe_produk: String,
    val tgl_mulai: String,
    val tgl_selesai: String,
    val jumlah_pesan: Int
)

data class ReservasiResponse(
    val success: Boolean,
    val message: String,
    val id_reservasi: Int
)

data class BayarRequest(
    val jumlah_dp: Int
)

data class BayarResponse(
    val success: Boolean,
    val message: String,
    val snap_token: String
)



