package com.example.uas_ppk

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RClient {
    public const val BASE_URL = "https://222011704.student.stis.ac.id/"
    val instance:api by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(api::class.java)
    }
}