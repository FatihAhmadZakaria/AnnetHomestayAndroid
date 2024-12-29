package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.annethomestay.databinding.FragmentHomeBinding
import com.example.annethomestay.utils.HomeViewModel

class FragmentHome : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Observe LiveData untuk update UI
        viewModel.promoLiveData.observe(viewLifecycleOwner) { promoList ->
            val adapter = DataListHomeAdapter(requireContext(), ArrayList(promoList))
            binding.listPromo.adapter = adapter

            binding.listPromo.setOnItemClickListener { _, _, position, _ ->
                val data = promoList[position]
                val i = Intent(requireContext(), ActivityPromo::class.java)
                i.putStringArrayListExtra("img_urls", data.img)
                i.putExtra("nama", data.nama)
                i.putExtra("deskripsi", data.deskrip)
                startActivity(i)
            }
        }

        // Fetch promo jika belum ada data
        if (viewModel.promoLiveData.value == null) {
            viewModel.fetchPromo()
        }

        // Set klik listener lainnya
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
