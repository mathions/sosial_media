package com.example.uas_ppk

import com.google.gson.annotations.SerializedName

data class ResponseProfile(
    @SerializedName("status")
    val stt:String,
    val totaldata:Int,
    val data:List<Profile>
)

class Profile (
    @SerializedName("id") val id:String,
    @SerializedName("username") val username:String,
    @SerializedName("userpassword") val userpassword:String,
    @SerializedName("useremail") val useremail:String,
    @SerializedName("fullname") val fullname:String,
    @SerializedName("image") val image:String
)
