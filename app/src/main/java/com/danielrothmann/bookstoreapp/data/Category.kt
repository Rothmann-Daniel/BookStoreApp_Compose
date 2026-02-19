package com.danielrothmann.bookstoreapp.data

data class Category(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val bookCount: Int = 0,
    val isActive: Boolean = true
)