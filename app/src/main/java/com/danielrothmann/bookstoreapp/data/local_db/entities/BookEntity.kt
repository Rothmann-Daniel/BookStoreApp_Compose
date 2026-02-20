package com.danielrothmann.bookstoreapp.data.local_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    val id: String = "",
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0
)
