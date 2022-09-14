package com.example.myapplication.db.repository

import android.app.Application
import androidx.paging.PagingSource
import com.example.myapplication.db.dao.BigFileDao
import com.example.myapplication.db.room.RoomDB
import com.example.myapplication.model.BigFileItem

class BigFileRepository (application: Application){
    companion object {
        private var instance: BigFileRepository? = null
        fun newInstance(application: Application): BigFileRepository {
            if (instance == null) {
                instance = BigFileRepository(application)
            }
            return instance!!
        }
    }

    private val bigFileDao = RoomDB.getInstance(application).bigFileDao()

    fun insert(list: List<BigFileItem>) {
        bigFileDao.insert(list)
    }
    fun delete() {
        bigFileDao.delete()
    }

    fun loadAll(): PagingSource<Int, BigFileItem> {
        return bigFileDao.loadAll()
    }

}