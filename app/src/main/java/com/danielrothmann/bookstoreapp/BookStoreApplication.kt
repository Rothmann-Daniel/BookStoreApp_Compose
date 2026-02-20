package com.danielrothmann.bookstoreapp

import android.app.Application
import com.danielrothmann.bookstoreapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BookStoreApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BookStoreApplication)
            modules(appModule)
        }
    }
}