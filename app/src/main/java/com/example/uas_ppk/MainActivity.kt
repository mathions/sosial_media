package com.example.uas_ppk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.uas_ppk.databinding.ActivityMainBinding
import com.example.uas_ppk.fragment.DataContentFragment
import com.example.uas_ppk.shared_preferences.PrefManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager(this)
        showDataFragment()

        binding.txtCari.setOnKeyListener(View.OnKeyListener{v,keyCode,event->

            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                showDataFragment()
                    return@OnKeyListener true
                }
            false
        })

        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnAdd.setOnClickListener{
            startActivity(Intent(this, FormAddContentActivity::class.java))
        }
    }

    private fun logout() {
        prefManager.removeData()
        startActivity(Intent(this, FormLoginActivity::class.java))
    }

    fun showDataFragment(){
        val mFragmentManager = supportFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = DataContentFragment()

        val textCari = binding.txtCari.text
        val mBundle = Bundle()
        mBundle.putString("cari",textCari.toString())
        mFragment.arguments = mBundle

        mFragmentTransaction.replace(R.id.fl_data,mFragment).commit()
    }
}