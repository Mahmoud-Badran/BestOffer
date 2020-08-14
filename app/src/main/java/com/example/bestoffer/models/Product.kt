package com.example.bestoffer.models


data class Category(
        val name: String,
        val isExpanded: Boolean,
        val products: List<Product>
)

data class Product(
        val pid: String? = null,
        val pname: String? = null,
        val price: String? = null,
        val description: String? = null,
        val image: String? = null,
        val category: String? = null,
        val time: String? = null,
        val date: String? = null,
        val categoryId: String? = null,
        var sellerCompany: String? = null,
        var sellerCompanyEmail: String? = null
)