package ru.markom.nework.repository.userWall.job

import androidx.lifecycle.MutableLiveData
import ru.markom.nework.dto.Job

interface MyWallJobRepository {
    val dateJob: MutableLiveData<MutableList<Job>>
    suspend fun getMyJob()
    suspend fun removeById(id: Int)
    suspend fun save(job: Job)
}