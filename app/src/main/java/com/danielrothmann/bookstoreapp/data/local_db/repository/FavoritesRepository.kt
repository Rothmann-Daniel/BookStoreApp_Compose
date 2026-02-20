package com.danielrothmann.bookstoreapp.data.local_db.repository

import com.danielrothmann.bookstoreapp.data.Book
import com.danielrothmann.bookstoreapp.data.local_db.dao.BookDao
import com.danielrothmann.bookstoreapp.data.mapper.BookLocalMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepository(
    private val bookDao: BookDao,
    private val mapper: BookLocalMapper
) {
    // Получаем все избранные книги как Flow
    fun getAllFavorites(): Flow<List<Book>> {
        return bookDao.getAll().map { entities ->
            entities.map { mapper.mapFromDb(it) }
        }
    }

    // Получаем одну книгу по ID
    suspend fun getFavoriteById(id: String): Book? {
        return bookDao.getById(id)?.let { mapper.mapFromDb(it) }
    }

    // Добавляем книгу в избранное
    suspend fun addToFavorites(book: Book) {
        val entity = mapper.toEntity(book)
        bookDao.insertAll(listOf(entity))
    }

    // Удаляем книгу из избранного
    suspend fun removeFromFavorites(bookId: String) {
        bookDao.deleteById(bookId)
    }

    // Проверяем, есть ли книга в избранном
    suspend fun isFavorite(bookId: String): Boolean {
        return bookDao.getById(bookId) != null
    }

    // Добавляем несколько книг (например, при синхронизации)
    suspend fun addAllToFavorites(books: List<Book>) {
        val entities = books.map { mapper.toEntity(it) }
        bookDao.insertAll(entities)
    }

    // Очищаем все избранное
    suspend fun clearAllFavorites() {
        bookDao.clearAll()
    }
}