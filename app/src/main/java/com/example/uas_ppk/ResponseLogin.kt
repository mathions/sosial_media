package com.example.uas_ppk

import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("status") val stt:Int,
    @SerializedName("error") val err:Int,
    @SerializedName("messages") val msg:String,
    val token:String,
    val email:String,
    val username:String
)
