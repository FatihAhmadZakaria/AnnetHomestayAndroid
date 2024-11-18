package com.example.annethomestay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.annethomestay.databinding.FragmentTransactionBinding
import com.example.annethomestay.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentTransaction : Fragment() {
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var transaksiList: ArrayList<Riwayat>
    private lateinit var adapter: DataListTransaksiAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        transaksiList = ArrayList()
        adapter = DataListTransaksiAdapter(requireContext(), transaksiList)

        val listView: ListView = binding.listTransaksi
        listView.adapter = adapter

        // Memanggil API untuk mendapatkan data transaksi
        getTransaksiData(userId.toString())

    }
    private fun getTransaksiData(userId: String) {
        ApiClient.apiService.getRiwayat(userId.toInt()).enqueue(object :
            Callback<RiwayatResponse> {  // Pastikan RiwayatResponse digunakan di sini
            override fun onResponse(call: Call<RiwayatResponse>, response: Response<RiwayatResponse>) {
                if (response.isSuccessful) {
                    val transaksi = response.body()?.data ?: emptyList()
                    transaksiList.clear()
                    transaksiList.addAll(transaksi)
                    adapter.notifyDataSetChanged()
                } else {
                    // Tangani error jika API tidak berhasil
                }
            }

            override fun onFailure(call: Call<RiwayatResponse>, t: Throwable) {
                // Tangani kegagalan API
            }
        })
    }

}