package com.bangkit.mountainapp.data.remote.api

import com.bangkit.mountainapp.data.remote.response.LoginResponse
import com.bangkit.mountainapp.data.remote.response.MountDetailResponse
import com.bangkit.mountainapp.data.remote.response.RegisterResponse
import com.bangkit.mountainapp.data.remote.response.FeedResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("/users")
    fun register(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("/mountain_detail")
    fun getTwoTopFavMount(): Call<MountDetailResponse>

    @Multipart
    @POST("/feeds")
    fun upload(
        @Part("username") name: String,
        @Part("upload_date") uploadDate: String,
        @Part("caption") caption: String,
        @Part file: MultipartBody.Part,
    ): Call<FeedResponse>

}