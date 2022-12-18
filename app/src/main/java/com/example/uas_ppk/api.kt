package com.example.uas_ppk

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface api {
    @GET("content/{cari}")
    fun getData(
        @Header("Authorization") token_auth:String?,
        @Path("cari") cari:String? = null
    ): Call<ResponseDataContent>

    @FormUrlEncoded
    @POST("content")
    fun createData(
        @Header("Authorization") token_auth:String?,
        @Field("username") username:String?,
        @Field("image") image:String?,
        @Field("caption") caption:String?,
        @Field("date") date:String?
        ):Call<ResponseCreate>

    @DELETE("content/{id}")
    fun deleteData(
        @Header("Authorization") token_auth:String?,
        @Path("id") id:String?
    ):Call<ResponseCreate>

    @FormUrlEncoded
    @PUT("content/{id}") fun updateData(
        @Header("Authorization") token_auth:String?,
        @Path("id") id: String?,
        @Field("caption") caption:String?
    ):Call<ResponseCreate>

    @GET("login")
    fun checkUserLogin(
        @Query("username") username:String,
        @Query("userpassword") userpassword:String,
    ):Call<ResponseLogin>

    @Multipart
    @POST("upload/{id}")
    fun uploadImage(
        @Path("id") id: String?,
        @Part foto: MultipartBody.Part,
        @Part("_method") _method:RequestBody
    ):Call<ResponseCreate>
}