package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.annethomestay.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentHome : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var promoArrayList: ArrayList<DataListHome>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiService = ApiClient.apiService
        val call = apiService.getPromo()
        call.enqueue(object : Callback<List<DataListHome>> {
            override fun onResponse(
                call: Call<List<DataListHome>>,
                response: Response<List<DataListHome>>
            ) {
                if (response.isSuccessful) {
                    val dataListPromo = response.body() ?: emptyList()
                    promoArrayList = ArrayList()

                    for (promo in dataListPromo) {
                        val dataListPromo = DataListHome(
                            img = promo.img,
                            nama = promo.nama,
                            deskrip = promo.deskrip
                        )
                        promoArrayList.add(dataListPromo)
                    }

                    binding.listPromo.isClickable = true
                    binding.listPromo.adapter = DataListHomeAdapter(requireContext(),promoArrayList)
                    binding.listPromo.setOnItemClickListener { parent, view, position, id ->
                        val data = dataListPromo[position]

                        val i = Intent(requireContext(), ActivityPromo::class.java)
                        i.putExtra("img", data.img)
                        i.putExtra("nama", data.nama)
                        i.putExtra("deskrip", data.deskrip)
                        startActivity(i)
                    }
                } else {
                    Log.d("gagal", "Panggilan API gagal: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<DataListHome>>, t: Throwable) {
                Log.d("gagal", "Panggilan API gagal: ${t.message}")
            }

        })


        binding.icHomestay.setOnClickListener {
            val i = Intent(requireContext(), ActivityPilihP::class.java)
            startActivity(i)
        }
        binding.icObjectRec.setOnClickListener {
            val i = Intent(requireContext(), ActivityPilihO::class.java)
            startActivity(i)
        }
        binding.icMotocycle.setOnClickListener {
            val i = Intent(requireContext(), ActivityPilihK::class.java)
            startActivity(i)
        }
    }
}