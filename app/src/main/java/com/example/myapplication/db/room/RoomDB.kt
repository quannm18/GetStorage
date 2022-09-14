package com.example.myapplication.db.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.db.dao.BigFileDao
import com.example.myapplication.model.BigFileItem

@Database(
    entities = [BigFileItem::class],
    version = 1,
    exportSchema = false
)
abstract class RoomDB : RoomDatabase() {
    abstract fun bigFileDao(): BigFileDao

    companion object {
        private const val DATABASE_NAME = "antivirus_pro"
        private var sInstance: RoomDB? = null
        fun getInstance(application: Application): RoomDB {
            synchronized(RoomDB::class.java) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                        application,
                        RoomDB::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return sInstance!!
        }
    }

}