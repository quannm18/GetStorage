package com.example.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "big_file",
    indices = [Index(value = arrayOf("id"))]
)
data class BigFileItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "logo")
    var logo: Int? = 0,
    @ColumnInfo(name = "size")
    var size: Long = 0,
    @ColumnInfo(name = "total_item")
    var totalItem: Int = 0,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "sub_title")
    var subTitle: String = "",
    @ColumnInfo(name = "path")
    var pathUri: String? = "",
    @ColumnInfo(name = "package_name")
    var packageName: String? = "",
    @ColumnInfo(name = "type")
    var type: TypeBigFile,
    @ColumnInfo(name = "is_checked")
    var isChecked: Boolean = false
)

enum class TypeBigFile {
    VIDEO, PHOTO, AUDIO, DOCUMENT, OTHER_FILE, HEAVY_APP
}