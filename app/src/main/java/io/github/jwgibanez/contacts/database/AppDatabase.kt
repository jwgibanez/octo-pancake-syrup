package io.github.jwgibanez.contacts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.jwgibanez.contacts.service.model.User
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DB_NAME = "app-db"
        private const val NUMBER_OF_THREADS = 1
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(
            NUMBER_OF_THREADS
        )
    }
}