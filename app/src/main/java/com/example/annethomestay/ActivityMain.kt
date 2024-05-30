package com.example.annethomestay

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.annethomestay.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val openFragment = intent.getStringExtra("open_fragment")
        if (openFragment != null) {
            when (openFragment) {
                "home" -> replaceFragment(FragmentHome(), "home")
                "transaction" -> replaceFragment(FragmentTransaction(), "transaction")
                "profile" -> replaceFragment(FragmentProfile(), "profile")
                else -> replaceFragment(FragmentHome(), "home")
            }
        } else {
            // Load default fragment
            replaceFragment(FragmentHome(), "home")
        }

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(FragmentHome(), "home")
                R.id.transaction -> replaceFragment(FragmentTransaction(), "transaction")
                R.id.profile -> replaceFragment(FragmentProfile(), "profile")
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_view, fragment)
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }
}
