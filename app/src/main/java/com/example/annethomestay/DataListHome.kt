package com.example.annethomestay

data class DataListHome(
    val img: ArrayList<String>,
    val nama: String,
    val deskrip: String
)

data class Promo(
    val img: List<Image>,
    val nama: String,
    val deskripsi: String
)

data class Image(
    val img_path: String
)
