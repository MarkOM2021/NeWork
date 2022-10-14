package ru.markom.nework.repository.usersWall

import androidx.lifecycle.MutableLiveData
import ru.markom.nework.api.ApiService
import ru.markom.nework.dto.Job
import ru.markom.nework.dto.UserRequest
import ru.markom.nework.error.ApiError
import ru.markom.nework.error.NetworkError
import java.io.IOException
import javax.inject.Inject

val listJob = listOf<Job>()
val user = UserRequest()

class UserWallRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : UserWallRepository {
    override val data: MutableLiveData<List<Job>> = MutableLiveData(listJob)
    override val userData: MutableLiveData<UserRequest> = MutableLiveData(user)

    override suspend fun getJobUser(id: String) {
        val usersList: List<Job>
        try {
            val response = apiService.getUserJob(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            usersList = response.body() ?: throw ApiError(response.code(), response.message())
            data.postValue(usersList)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getUser(id: Int) {
        val user: UserRequest
        try {
            val response = apiService.getUser(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            user = response.body() ?: throw ApiError(response.code(), response.message())
            userData.postValue(user)
        } catch (e: IOException) {
            throw NetworkError
        }
    }
}