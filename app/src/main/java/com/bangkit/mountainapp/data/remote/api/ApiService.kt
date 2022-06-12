package com.bangkit.mountainapp.data.remote.api

import com.bangkit.mountainapp.data.remote.response.LoginResponse
import com.bangkit.mountainapp.data.remote.response.MountDetailResponse
import com.bangkit.mountainapp.data.remote.response.RegisterResponse
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
}