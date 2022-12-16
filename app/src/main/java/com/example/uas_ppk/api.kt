package com.example.uas_ppk

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface api {
    @GET("content/{cari}")
    fun getData(@Path("cari") cari:String? = null): Call<ResponseDataContent>

    @FormUrlEncoded
    @POST("content")
    fun createData(
        @Field("username") username:String?,
        @Field("image") image:String?,
        @Field("caption") caption:String?,
        @Field("date") date:String?
        ):Call<ResponseCreate>
}