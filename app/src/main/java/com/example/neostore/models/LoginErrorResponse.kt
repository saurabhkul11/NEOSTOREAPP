package com.example.neostore.models

data class LoginErrorResponse(
    val message: String,
    val status: Int,
    val user_msg: String
)