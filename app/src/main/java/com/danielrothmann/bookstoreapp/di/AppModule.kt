package com.danielrothmann.bookstoreapp.di

import android.content.Context
import androidx.room.Room
import com.danielrothmann.bookstoreapp.data.local_db.dao.BookDao
import com.danielrothmann.bookstoreapp.data.local_db.database.AppDatabase
import com.danielrothmann.bookstoreapp.data.local_db.repository.FavoritesRepository
import com.danielrothmann.bookstoreapp.data.mapper.BookLocalMapper
import com.danielrothmann.bookstoreapp.favourites.FavoritesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    // DAO
    single<BookDao> { get<AppDatabase>().bookDao() }

    // Mapper
    single { BookLocalMapper() }

    // Repository
    single { FavoritesRepository(get(), get()) }

    // ViewModel
    viewModel { FavoritesViewModel(get()) }
}