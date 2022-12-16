package com.example.uas_ppk

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface api {
    @GET("produk/{cari}")
    fun getData(@Path("cari") cari:String? = null): Call<ResponseDataProduk>
}