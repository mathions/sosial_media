package com.example.uas_ppk

import retrofit2.Call
import retrofit2.http.*

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

    @DELETE("content/{id}")
    fun deleteData(@Path("id") id:String?):Call<ResponseCreate>
}