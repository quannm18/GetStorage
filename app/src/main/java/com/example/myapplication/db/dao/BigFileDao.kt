package com.example.myapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.BigFileItem

@Dao
interface BigFileDao {
    @Query("SELECT count (*) FROM big_file")
    fun getCount(): LiveData<Int>

    @Query("SELECT * FROM big_file where is_checked = 0 order by id asc")
    fun loadAll(): PagingSource<Int, BigFileItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bigFileItem: BigFileItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<BigFileItem>)

    @Query("delete from big_file")
    fun delete()
}