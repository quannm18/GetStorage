package com.example.myapplication.viewmodel

import android.app.Application
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.R
import com.example.myapplication.db.repository.BigFileRepository
import com.example.myapplication.model.BigFileItem
import com.example.myapplication.model.TypeBigFile
import kotlinx.coroutines.*

class BigFileViewModel(application: Application) :
    AndroidViewModel(application) {

    private val bigFileRepository: BigFileRepository by lazy {
        BigFileRepository.newInstance(application)
    }

    val listenEvent = MutableLiveData<Any>()
    val event: LiveData<Any> by lazy {
        listenEvent
    }
    val listImage: kotlinx.coroutines.flow.Flow<PagingData<BigFileItem>> by lazy {
        Pager(
            PagingConfig(
                30,
                enablePlaceholders = true,
                initialLoadSize = 30
            )
        ) {
            bigFileRepository.loadAll()
        }.flow.cachedIn(viewModelScope)
    }

    fun insert(list: List<BigFileItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            bigFileRepository.insert(list)
        }
    }

    fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            bigFileRepository.delete()
        }
    }

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.async(Dispatchers.IO) {
            listenEvent.postValue(State.LOADING)
            Log.e(TAG, "loadData: Begin")

            val deleteJob = async { bigFileRepository.delete() }
            deleteJob.await()
            Log.e(TAG, "Delete ")

            val job = async {
                bigFileRepository.insert(loadPhotosFromExternalStorage())
                Log.e(TAG, "loadData: ${loadVideoFromExternalStorage().size}")
            }
            job.await()
            listenEvent.postValue(State.LOADED)
            Log.d(TAG, "loadData: END")
        }
    }


    private suspend fun loadPhotosFromExternalStorage(): List<BigFileItem> {
        return withContext(Dispatchers.IO) {
            val photos = mutableListOf<BigFileItem>()
            val imageCollection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.SIZE,
            )
            getApplication<Application>().contentResolver.query(
                imageCollection,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                cursor.moveToFirst()
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val size = cursor.getLong(sizeColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)
                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    photos.add(
                        BigFileItem(
                            id = id.toInt(),
                            size = size,
                            name = displayName,
                            type = TypeBigFile.PHOTO,
                            pathUri = contentUri.toString(),
                            isChecked = false
                        )
                    )
                }
            }

            Log.e("size", "${photos.size}")
            photos.toList()
        }
    }

    private suspend fun loadVideoFromExternalStorage(): List<BigFileItem> {
        return withContext(Dispatchers.IO) {
            val photos = mutableListOf<BigFileItem>()
            val imageCollection = sdk29AndUp {
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.SIZE,
            )
            getApplication<Application>().contentResolver.query(
                imageCollection,
                projection,
                null,
                null,
                "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                cursor.moveToFirst()
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val size = cursor.getLong(sizeColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)
                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                    photos.add(
                        BigFileItem(
                            id = id.toInt(),
                            size = size,
                            name = displayName,
                            type = TypeBigFile.PHOTO,
                            pathUri = contentUri.toString(),
                            isChecked = false
                        )
                    )
                }
            }

            Log.e("size", "${photos.size}")
            photos.toList()
        }
    }

    private suspend fun loadAudioFromExternalStorage(): List<BigFileItem> {
        return withContext(Dispatchers.IO) {
            val photos = mutableListOf<BigFileItem>()
            val imageCollection = sdk29AndUp {
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.WIDTH,
                MediaStore.Audio.Media.HEIGHT,
                MediaStore.Audio.Media.SIZE,
            )
            getApplication<Application>().contentResolver.query(
                imageCollection,
                projection,
                null,
                null,
                "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.WIDTH)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                cursor.moveToFirst()
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val size = cursor.getLong(sizeColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)
                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                    photos.add(
                        BigFileItem(
                            id = id.toInt(),
                            size = size,
                            name = displayName,
                            type = TypeBigFile.PHOTO,
                            pathUri = contentUri.toString(),
                            isChecked = false,
                            logo = R.drawable.ic_launcher_foreground
                        )
                    )
                }
            }

            Log.e("size", "${photos.size}")
            photos.toList()
        }
    }

    private fun loadDocumentFromExternalStorage(): MutableList<BigFileItem> {
        return runBlocking(Dispatchers.IO) {
            val photos = mutableListOf<BigFileItem>()
            val imageCollection = sdk29AndUp {
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val projection = arrayOf(
                MediaStore.Files.FileColumns.DOCUMENT_ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.RELATIVE_PATH,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_TAKEN,
                MediaStore.Files.FileColumns.SIZE,
            )
            getApplication<Application>().contentResolver.query(
                imageCollection,
                projection,
                null,
                null,
                null
            )?.use { cursor ->
                val idColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DOCUMENT_ID)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val relativePathColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.RELATIVE_PATH)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                val mimeTypeColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
                val dateTakenColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_TAKEN)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)

                cursor.moveToFirst()

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val relativePath = cursor.getString(relativePathColumn)
                    val data = cursor.getString(dataColumn)
                    val mimeType = cursor.getString(mimeTypeColumn)
                    val timeMillis = cursor.getLong(dateTakenColumn)
                    val size = cursor.getLong(sizeColumn)
                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    if (displayName.endsWith(".DNG", true) ||
                        displayName.endsWith(".WEBP", true) ||
                        displayName.contains(".xls", true) ||
                        displayName.contains(".doc", true) ||
                        displayName.contains(".ppt", true)
                    ) {
                        photos.add(
                            BigFileItem(
                                id = id.toInt(),
                                size = size,
                                name = displayName,
                                type = TypeBigFile.PHOTO,
                                pathUri = "/storage/emulated/0/${relativePath}${displayName}",
                                isChecked = false,
                                logo = R.drawable.ic_launcher_foreground
                            )
                        )
                    }
                }
            }
            photos
        }
    }

    private fun <T> sdk29AndUp(onSdk29: () -> T): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            onSdk29.invoke()
        } else null
    }

    enum class State {
        LOADING, LOADED, LOAD_FAIL
    }

    companion object {
        var TAG: String = javaClass.name
    }
}