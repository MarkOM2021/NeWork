package ru.markom.nework.repository.usersWall


import androidx.lifecycle.MutableLiveData
import ru.markom.nework.dto.Job
import ru.markom.nework.dto.UserRequest

interface UserWallRepository {
    val data: MutableLiveData<List<Job>>
    val userData: MutableLiveData<UserRequest>
    suspend fun getJobUser(id: String)
    suspend fun getUser(id: Int)
}