package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.annethomestay.databinding.FragmentHomeBinding

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
        val imgId = arrayOf(R.drawable.gladag1, R.drawable.limasan1)
        val deskrip = arrayOf("Gladag Promo", "Limasan Promo")

        promoArrayList = ArrayList()

        for ( i in imgId.indices){
            val promo = DataListHome(imgId[i], deskrip[i])
            promoArrayList.add(promo)
        }

        binding.listPromo.isClickable = true
        binding.listPromo.adapter = DataListHomeAdapter(requireContext(),promoArrayList)
        binding.listPromo.setOnItemClickListener { parent, view, position, id ->
            val imgId = imgId[position]
            val deskrip = deskrip[position]

            val i = Intent(requireContext(), ActivityPromo::class.java)
            i.putExtra("img", imgId)
            i.putExtra("deskrip", deskrip)
            startActivity(i)
        }
        binding.icObjectRec.setOnClickListener {
            val i = Intent(requireContext(), ActivityPilihO::class.java)
            startActivity(i)
        }
    }
}