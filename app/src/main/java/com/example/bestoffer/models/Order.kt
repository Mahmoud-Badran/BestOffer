package com.example.bestoffer.models

data class Order(
        val email: String,
        val paymentMethod: String,
        val productId: String,
        val productName: String,
        val productPrice: String
)

