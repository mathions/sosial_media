package com.example.uas_ppk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.uas_ppk.databinding.ActivityFormUploadBinding

class FormUploadActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFormUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}