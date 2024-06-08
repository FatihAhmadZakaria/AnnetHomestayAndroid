package com.example.annethomestay

data class TransaksiKendaraan(
    val id_k: Int,
    val id_p: Int,
    val id_mtd: Long,
    val user_id: Int,
    val durasi_trans_k: Int,
    val stat_trans_k: String,
    val total_bayar: Int
)
