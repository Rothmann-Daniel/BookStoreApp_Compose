package com.danielrothmann.bookstoreapp.data.local_db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.danielrothmann.bookstoreapp.data.local_db.dao.BookDao
import com.danielrothmann.bookstoreapp.data.local_db.entities.BookEntity

@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}
