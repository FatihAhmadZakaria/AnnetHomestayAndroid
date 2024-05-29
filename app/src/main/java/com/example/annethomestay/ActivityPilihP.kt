package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityPilihPBinding

class ActivityPilihP : AppCompatActivity() {
    private lateinit var binding: ActivityPilihPBinding
    private lateinit var penginapanArrayList: ArrayList<DataListPenginapan>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPilihPBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val img = arrayOf(R.drawable.glamping1, R.drawable.limasan1)
        val imgSpin = arrayOf("img_glamping", "img_limasan")
        val name = arrayOf("Glamping", "Limasan")
        val deskrip = arrayOf("Paket Glamping", "Paket Limasan")
        val kapasitas = arrayOf("2-4", "2-5")
        val harga = arrayOf(500000, 750000)

        penginapanArrayList = ArrayList()

        for ( i in img.indices){
            val penginapan = DataListPenginapan(img[i], imgSpin[i], name[i],
                deskrip[i], kapasitas[i], harga[i])
            penginapanArrayList.add(penginapan)
        }

        binding.listPenginapan.isClickable = true
        binding.listPenginapan.adapter = DataListPenginapanAdapter(this, penginapanArrayList)
        binding.listPenginapan.setOnItemClickListener { parent, view, position, id ->
            val img = img[position]
            val imgSpin = imgSpin[position]
            val name = name[position]
            val deskrip = deskrip[position]
            val kapasitas = kapasitas[position]
            val harga = harga[position]

            val i = Intent(this, ActivityPesanPenginapan::class.java)
            i.putExtra("img",img)
            i.putExtra("imgSpin",imgSpin)
            i.putExtra("name",name)
            i.putExtra("deskrip",deskrip)
            i.putExtra("kapasitas",kapasitas)
            i.putExtra("harga",harga)
            startActivity(i)
        }
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }
}