package com.bangkit.mountainapp.model

data class UserModel(
    var userId: String,
    var name: String,
    var token: String,
    var isLogin: Boolean
)
