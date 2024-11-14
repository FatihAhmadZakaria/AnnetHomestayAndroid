package com.example.annethomestay

data class DataListKendaraan(
    val id: Int,
    val img: ArrayList<String>,
    val nama: String,
    val harga: String,
    val deskrip: String
)

data class Kendaraan(
    val id: Int,
    val img: List<Images>,
    val nama: String,
    val harga: String,
    val deskrip: String
)

data class Images(
    val img_path: String
)