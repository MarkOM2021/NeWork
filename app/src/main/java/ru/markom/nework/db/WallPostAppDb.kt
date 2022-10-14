package ru.markom.nework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.markom.nework.dao.*
import ru.markom.nework.dao.MyWallPostDao
import ru.markom.nework.dao.MyWallRemoteKeyDao
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
abstract class WallPostAppDb : RoomDatabase() {
    abstract fun myWallPostDao(): MyWallPostDao
    abstract fun myWallPostRemoteKeyDao(): MyWallRemoteKeyDao
}