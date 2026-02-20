package com.danielrothmann.bookstoreapp.data.mapper

import com.danielrothmann.bookstoreapp.data.Book
import com.danielrothmann.bookstoreapp.data.local_db.entities.BookEntity

class BookLocalMapper {
    /**
     * Преобразует модель книги в entity для БД
     */
    fun toEntity(book: Book): BookEntity {
        return BookEntity(
            id = book.id,
            title = book.title,
            author = book.author,
            description = book.description,
            category = book.category,
            imageUrl = book.imageUrl,
            price = book.price
        )
    }

    /**
     * Преобразует entity из БД в domain модель
     */
    fun mapFromDb(entity: BookEntity): Book {
        return Book(
            id = entity.id,
            title = entity.title,
            author = entity.author,
            description = entity.description,
            category = entity.category,
            imageUrl = entity.imageUrl,
            price = entity.price
        )
    }
}