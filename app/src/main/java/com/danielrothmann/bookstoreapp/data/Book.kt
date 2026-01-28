package com.danielrothmann.bookstoreapp.data

data class Book(
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0
) {
    // Обязательный пустой конструктор для Firestore
    constructor() : this("", "", "", "", "", 0.0)
}