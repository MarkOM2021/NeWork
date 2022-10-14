package ru.markom.nework.repository.user

import ru.markom.nework.dto.UserRegistration
import ru.markom.nework.dto.UserResponse

interface UserRepository {
    suspend fun onSignIn(userResponse: UserResponse)
    suspend fun onSignUp(user: UserRegistration)
}