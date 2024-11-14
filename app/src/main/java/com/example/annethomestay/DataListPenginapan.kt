package com.example.annethomestay

data class DataListPenginapan(
    val id: Int,
    val img: ArrayList<String>,
    val nama: String,
    val deskrip: String?,
    val fitur: String,
    val kapasitas: String,
    val harga: Int
)

data class Penginapan(
    val id: Int,
    val img: List<Imagez>,
    val nama: String,
    val deskrip: String,
    val fitur: String,
    val kapasitas: String,
    val harga: Int
)

data class Imagez(
    val img_path: String
)
