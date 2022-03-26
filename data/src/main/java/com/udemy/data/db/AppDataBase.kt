package com.udemy.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udemy.data.models.ShopItemDbModel

@Database(entities = [ShopItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {

    abstract fun shopListDao(): ShopListDao

    companion object {
        private var INSTANCE: AppDataBase? = null
        private val LOCK = Any() // для синхорнизации доступа к базе из разных потоков
        private const val DB_NAME = "shop_item.db"

        fun getInstance(application: Application): AppDataBase {
            // Проверка если база уже создана
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                // Ещё раз проверка, на случай если другой поток уже создал,
                // а текущий поток не заметил этого.
                INSTANCE?.let {
                    return it
                }
                // Создание базы данных
                val db = Room.databaseBuilder(
                    application,
                    AppDataBase::class.java,
                    DB_NAME
                )
//                    .allowMainThreadQueries() // Разрешает выполнять запросы в главном потоке
                    .build()
                INSTANCE = db
                // Мы не можем вернуть INSTANCE так как она нуллабельна
                return db
            }
        }
    }
}