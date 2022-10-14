package ru.markom.nework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.markom.nework.dao.Converters
import ru.markom.nework.dao.JobDao
import ru.markom.nework.entity.JobEntity

@Database(entities = [JobEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class JobAppDb : RoomDatabase() {
    abstract fun jobDao(): JobDao
}