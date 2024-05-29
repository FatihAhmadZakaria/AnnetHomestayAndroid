package com.example.annethomestay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.annethomestay.databinding.ActivityPilihOBinding

class ActivityPilihO : AppCompatActivity() {
    private lateinit var binding: ActivityPilihOBinding
    private lateinit var objekArrayList: ArrayList<DataListRekomendasi>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihOBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imgObj = arrayOf(R.drawable.jejamuran, R.drawable.obelix)
        val nameObj = arrayOf("JeJamuran", "Obelix Village")
        val deskrip = arrayOf(getString(R.string.jejamuran), getString(R.string.obelix))
        val linkObj = arrayOf(getString(R.string.maps_jejamuran), getString(R.string.maps_obelix))

        objekArrayList = ArrayList()

        for ( i in imgObj.indices){
            val objek = DataListRekomendasi(imgObj[i], nameObj[i], deskrip[i].toString(),
                linkObj[i].toString()
            )
            objekArrayList.add(objek)
        }

        binding.listRekObj.isClickable = true
        binding.listRekObj.adapter = DataListRekomendasiAdapter(this,objekArrayList)
        binding.listRekObj.setOnItemClickListener { parent, view, position, id ->
            val imgObj = imgObj[position]
            val nameObj = nameObj[position]
            val deskrip = deskrip[position]
            val linkObj = linkObj[position]

            val i = Intent(this, ActivityDetailObjek::class.java)
            i.putExtra("img", imgObj)
            i.putExtra("name", nameObj)
            i.putExtra("deskrip", deskrip)
            i.putExtra("link", linkObj)
            startActivity(i)
        }
        binding.icBackObjek.setOnClickListener {
            onBackPressed()
        }
    }
}