package com.example.annethomestay.utils

interface TransaksiActionListener {
    fun onBayarClicked(snapToken: String)
    fun onBatalkanClicked(idReservasi: Int)
}