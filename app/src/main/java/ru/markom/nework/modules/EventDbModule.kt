package ru.markom.nework.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.markom.nework.dao.EventDao
import ru.markom.nework.dao.EventRemoteKeyDao
import ru.markom.nework.db.EventAppDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class EventDbModule {

    @Singleton
    @Provides
    fun provideAppDb(
        @ApplicationContext context: Context
    ): EventAppDb = Room.databaseBuilder(context, EventAppDb::class.java, "events.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideEventDao(appDb: EventAppDb): EventDao = appDb.eventDao()

    @Provides
    fun provideEventRemoteKeyDao(appDb: EventAppDb): EventRemoteKeyDao = appDb.eventRemoteKeyDao()
}