package com.example.uas_ppk

import com.google.gson.annotations.SerializedName

class ProdukData (
    @SerializedName("produk_nama") val nama:String,
    @SerializedName("produk_harga") val harga:String,
    @SerializedName("produk_gambar") val gambar:String,
)