package com.example.annethomestay

data class DataListTransaksi(
    val id: Int,
    val tgl: String,
    val jenis: String,
    val nama: String,
    val durasi: Int,
    val stat: String
)

data class TransactionRequest(
    val total_price: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val payment_type: String
)

data class MidtransResponse(
    val token: String,
    val redirect_url: String?
)

data class RiwayatResponse(
    val data: List<Riwayat>,
    val message: String
)

data class Riwayat(
    val nama_produk: String,
    val jenis: String,
    val status: String,
    val total: Int,
    val tgl_mulai: String,
    val tgl_selesai: String,
    val tgl_transaksi: String
)
