package com.example.neostore.models

data class ForgotPasswordResponse(
    val message: String,
    val status: Int,
    val user_msg: String
)