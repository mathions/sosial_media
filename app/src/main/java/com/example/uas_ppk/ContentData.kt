package com.example.uas_ppk

import com.google.gson.annotations.SerializedName

class ContentData (
    @SerializedName("id") val id:String,
    @SerializedName("username") val username:String,
    @SerializedName("image") val image:String,
    @SerializedName("caption") val caption:String,
    @SerializedName("date") val date:String,
)