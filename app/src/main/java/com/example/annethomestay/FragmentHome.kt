package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        call.enqueue(object : Callback<List<Promo>> {
            override fun onResponse(
                call: Call<List<Promo>>,
                response: Response<List<Promo>>
            ) {
                if (response.isSuccessful) {
                    val promoList = response.body() ?: emptyList()
                    promoArrayList = ArrayList()
                    Log.d("PromoResponse", "Response JSON: ${response.body()}")

                    // Memproses data promo dan gambar
                    for (promo in promoList) {
                        val imageUrls = promo.img.map { "https://annet.nosveratu.com/storage/app/public/${it.img_path}" } // List gambar
                        val fullImageUrlList = ArrayList(imageUrls)

                        val dataListPromo = DataListHome(
                            img = fullImageUrlList,
                            nama = promo.nama,
                            deskrip = promo.deskripsi
                        )

                        promoArrayList.add(dataListPromo)
                    }


                    // Menetapkan adapter dan menampilkan data di ListView
                    binding.listPromo.isClickable = true
                    binding.listPromo.adapter = DataListHomeAdapter(requireContext(), promoArrayList)

                    binding.listPromo.setOnItemClickListener { _, _, position, _ ->
                        val data = promoArrayList[position]

                        // Membuat Intent untuk mengirimkan data ke ActivityPromo
                        val i = Intent(requireContext(), ActivityPromo::class.java)

                        // Mengirimkan list gambar ke ActivityPromo
                        i.putStringArrayListExtra("img_urls", ArrayList(data.img))  // Kirim list gambar
                        i.putExtra("nama", data.nama)
                        i.putExtra("deskripsi", data.deskrip)

                        startActivity(i)
                    }
                } else {
                    Log.d("gagal", "Panggilan API gagal: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Promo>>, t: Throwable) {
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