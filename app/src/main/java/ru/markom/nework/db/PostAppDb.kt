package ru.markom.nework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.markom.nework.dao.*
import ru.markom.nework.entity.PostEntity
import ru.markom.nework.entity.PostRemoteKeyEntity

@Database(
    entities = [PostEntity::class, PostRemoteKeyEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(
    Converters::class, CoordinatesConverter::class,
    ConvertersListIds::class
)
abstract class PostAppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
}