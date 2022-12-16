package com.example.uas_ppk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FormAddProdukActivity : AppCompatActivity() {
    private lateint var binding : ActivityFormAddProdukBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormAddProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}