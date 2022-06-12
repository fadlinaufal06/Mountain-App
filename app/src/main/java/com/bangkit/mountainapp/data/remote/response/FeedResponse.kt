package com.bangkit.mountainapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class FeedResponse(
    @field:SerializedName("result")
    val result: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val feedResult: FeedResult
)

data class FeedResult(
    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("upload_date")
    val uploadDate: String,

    @field:SerializedName("caption")
    val caption: String,

    @field:SerializedName("photo")
    val photo: String
)

