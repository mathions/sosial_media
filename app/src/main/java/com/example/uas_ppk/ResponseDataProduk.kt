package com.example.uas_ppk

import com.google.gson.annotations.SerializedName

class ResponseDataProduk (
    @SerializedName("status") val stt:String,
            val totaldata:Int,
            val data:List<ProdukData>
)