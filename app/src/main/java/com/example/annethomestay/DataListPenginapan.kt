package com.example.annethomestay

data class DataListPenginapan(
    val id: Int,
    val img: ArrayList<String>,
    val nama: String,
    val deskrip: String?,
    val fitur: String,
    val kapasitas: Int,
    val harga: Int
)

data class Penginapan(
    val id: Int,
    val img: List<Imagez>,
    val nama: String,
    val deskrip: String,
    val fitur: String,
    val kapasitas: Int,
    val harga: Int
)

data class Imagez(
    val img_path: String
)

data class CekKetersediaanRequest(
    val tgl_mulai: String,
    val tgl_selesai: String
)

data class CekKetersediaanResponse(
    val success: Boolean,
    val ketersediaan: Int,
)
data class CekKetersediaanResponseKen(
    val success: Boolean,
    val ketersediaan: String,
)

