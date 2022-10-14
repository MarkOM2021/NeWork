package ru.markom.nework.repository.userWall.job

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface MyWallJobRepositoryModule {
    @Binds
    @Singleton
    fun bindMyWallJobRepository(impl: MyWallJobRepositoryImpl): MyWallJobRepository
}