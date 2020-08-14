package com.example.bestoffer.models

data class User(
        val name: String? = null,
        val phone: String? = null,
        val password: String? = null,
        val image: String? = null,
        val address: String? = null,
        val email: String? = null,
        val userType: String? = null
)

data class UiUser(
        val name: String,
        val phone: String,
        val password: String,
        val image: String? = null,
        val address: String? = null,
        val email: String,
        val userType: UserType
)

enum class UserType {
    USER,
    ADMIN,
    COMPANY
}