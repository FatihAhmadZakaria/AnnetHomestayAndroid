package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.example.annethomestay.databinding.FragmentTransactionBinding
import com.example.annethomestay.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentTransaction : Fragment() {
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var transaksiList: ArrayList<DataListTransaksi>
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
        binding.listTransaksi.adapter = adapter
        binding.listTransaksi.setOnItemClickListener { parent, view, position, id ->
            val data = transaksiList[position]
            val intent = Intent(requireContext(), ActivityDetailTransaction::class.java)
            intent.putExtra("id_trans", data.id)
            intent.putExtra("tgl", data.tgl)
            intent.putExtra("jenis", data.jenis)
            intent.putExtra("nama", data.nama)
            intent.putExtra("durasi", data.durasi)
            intent.putExtra("stat", data.stat)
            startActivity(intent)
        }

        if (userId != -1) {
            fetchTransactionHistory(userId)
        } else {
            Toast.makeText(context, "User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

    }

    private fun fetchTransactionHistory(id: Int) {
        val apiService = ApiClient.apiService
        val call = apiService.getRiwayat(id)

        call.enqueue(object : Callback<List<DataListTransaksi>> {
            override fun onResponse(call: Call<List<DataListTransaksi>>, response: Response<List<DataListTransaksi>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        transaksiList.clear()
                        transaksiList.addAll(it)
                        adapter.notifyDataSetChanged()
                        for (transaksi in it) {
                            Log.d("Hallo bro", "onResponse: ${transaksi.id}")
                        }
                    }

                } else {
                    Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<DataListTransaksi>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}