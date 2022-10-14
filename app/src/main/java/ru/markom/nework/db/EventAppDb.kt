package ru.markom.nework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.markom.nework.dao.*
import ru.markom.nework.entity.EventEntity
import ru.markom.nework.entity.EventRemoteKeyEntity

@Database(
    entities = [EventEntity::class, EventRemoteKeyEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(
    Converters::class, CoordinatesConverter::class, EventTypeConverters::class,
    ConvertersListIds::class
)
abstract class EventAppDb : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun eventRemoteKeyDao(): EventRemoteKeyDao
}