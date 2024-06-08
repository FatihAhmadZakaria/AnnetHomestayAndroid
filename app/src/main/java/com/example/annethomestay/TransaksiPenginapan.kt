package com.example.annethomestay

data class TransaksiPenginapan(
    val id_p: Int,
    val id_mtd: Long,
    val user_id: Int,
    val tgl_in: String,
    val tgl_out: String,
    val durasi_trans_p: Int,
    val stat_trans_p: String,
    val total_bayar: Int,
)
