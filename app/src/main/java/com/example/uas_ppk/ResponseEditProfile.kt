package com.example.uas_ppk

import com.google.gson.annotations.SerializedName

data class ResponseEditProfile(
    @SerializedName("status") val stt:Int,
    @SerializedName("error") val e:Boolean,
    @SerializedName("message") val pesan:String,
)
