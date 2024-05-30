package com.example.annethomestay

import java.io.Serializable

data class Penginapan(
    val idTransaksi: String,
    val namaPenginapan: String,
    val jenisTransaksi: String,
    val statusTransaksi: String,
    val durasiSewa: Int,
    val tanggalSewa: String,
    val statusPembayaran: String,
    val totalTransaksi: Int
) : java.io.Serializable

data class Kendaraan(
    val idTransaksi: String,
    val namaKendaraan: String,
    val jenisTransaksi: String,
    val statusTransaksi: String,
    val durasiSewa: Int,
    val tanggalSewa: String,
    val statusPembayaran: String,
    val totalTransaksi: Int
) : Serializable

