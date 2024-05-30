package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.annethomestay.databinding.FragmentTransactionBinding

class FragmentTransaction : Fragment() {
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var transaksiList: ArrayList<Any>
    private lateinit var adapter: DataListTransactionAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transaksiList = ArrayList()
        // Tambahkan data ke transaksiList (data dari dua tabel)
        transaksiList.add(Penginapan("1", "Hotel XYZ", "Penginapan", "Berhasil", 3, "2024-05-01", "Dibayar", 300000))
        transaksiList.add(Kendaraan("2", "Mobil ABC", "Kendaraan", "Berhasil", 2, "2024-05-02", "Dibayar", 500000))

        adapter = DataListTransactionAdapter(requireContext(), transaksiList)
        binding.listTransaksi.adapter = adapter

        binding.listTransaksi.setOnItemClickListener { parent, view, position, id ->
            val transaksi = transaksiList[position]
            val toastMessage = if (transaksi is Penginapan) {
                "Anda memilih transaksi penginapan"
            } else if (transaksi is Kendaraan) {
                "Anda memilih transaksi kendaraan"
            } else {
                "Transaksi tidak diketahui"
            }
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), ActivityDetailTransaction::class.java)
            if (transaksi is Penginapan) {
                intent.putExtra("jenis_transaksi", "penginapan")
                intent.putExtra("transaksi", transaksi)
            } else if (transaksi is Kendaraan) {
                intent.putExtra("jenis_transaksi", "kendaraan")
                intent.putExtra("transaksi", transaksi)
            }
            startActivity(intent)
        }

    }
}