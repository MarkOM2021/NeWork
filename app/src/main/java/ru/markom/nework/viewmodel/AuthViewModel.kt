package ru.markom.nework.viewmodel


import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.markom.nework.auth.AppAuth
import ru.markom.nework.auth.AuthState

import ru.markom.nework.dto.UserRegistration
import ru.markom.nework.dto.UserResponse
import ru.markom.nework.repository.user.UserRepository
import ru.markom.nework.error.UnknownError
import ru.markom.nework.model.ErrorLive

import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appAuth: AppAuth,
    private val repositoryUser: UserRepository
) : ViewModel() {
    private val _errors = MutableLiveData(ErrorLive())
    val errors: LiveData<ErrorLive>
        get() = _errors
    val data: LiveData<AuthState> = appAuth
        .authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = appAuth.authStateFlow.value.id != 0L


    fun onSignIn(userResponse: UserResponse) {
        viewModelScope.launch {
            try {
                repositoryUser.onSignIn(userResponse)
                _errors.value = ErrorLive(error = false)
            } catch (e: RuntimeException) {
                _errors.value = ErrorLive(error = true)
            }
        }
    }

    fun onSignUp(user: UserRegistration) {
        viewModelScope.launch {
            try {
                repositoryUser.onSignUp(user)
            } catch (e: Exception) {
                throw UnknownError
            }
        }
    }
}