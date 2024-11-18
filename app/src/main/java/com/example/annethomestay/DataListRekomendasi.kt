package com.example.annethomestay

data class DataListRekomendasi(
    val img: ArrayList<String>,
    val nama: String,
    val deskripsi: String,
    val link: String
)

data class Objek(
    val img: List<Imeg>,
    val nama: String,
    val deskripsi: String,
    val link: String
)

data class Imeg(
    val img_path: String
)

