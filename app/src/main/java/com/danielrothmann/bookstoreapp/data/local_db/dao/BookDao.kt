package com.danielrothmann.bookstoreapp.data.local_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.danielrothmann.bookstoreapp.data.local_db.entities.BookEntity

@Dao
interface BookDao{
    @Query("SELECT * FROM books")
    suspend fun getAll(): List<BookEntity>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getById(id: String): BookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vacancies: List<BookEntity>)

    @Query("DELETE FROM books")
    suspend fun clearAll()
}
