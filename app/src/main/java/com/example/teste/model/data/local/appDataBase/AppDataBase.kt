package com.example.teste.model.data.local.appDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.teste.model.data.local.dao.UserDAO
import com.example.teste.model.data.local.entity.User

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN passWordHash TEXT DEFAULT ''")
                db.execSQL("ALTER TABLE users ADD COLUMN accessType TEXT DEFAULT 'user'")
            }
        }

        fun getInstance(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_2) // Adiciona as migrações
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
