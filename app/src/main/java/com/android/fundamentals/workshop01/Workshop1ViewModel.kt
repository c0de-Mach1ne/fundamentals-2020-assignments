package com.android.fundamentals.workshop01

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class Workshop1ViewModel(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _mutableLoginState = MutableLiveData<LoginResult>()
    private val _mutableLogoutState = MutableLiveData<LogoutResult>()

    val loginState: LiveData<LoginResult> get() = _mutableLoginState
    val logoutState: LiveData<LogoutResult> get() = _mutableLogoutState

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _mutableLoginState.value = LoginResult.Loading()

                withContext(Dispatchers.IO) {
                    delay(DELAY_MILLIS)
                }

                _mutableLoginState.value = when {
                    userName.isEmpty() -> LoginResult.Error.UserName()
                    password.isEmpty() -> LoginResult.Error.Password()
                    else -> {
                        val newToken = UUID.randomUUID().toString()
                        // TODO create updateUserToken fun that will add or update user token in SP

                        LoginResult.Success()
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _mutableLogoutState.value = LogoutResult.Loading()

                withContext(Dispatchers.IO) {
                    delay(DELAY_MILLIS)
                }

                // TODO create clearUserToken fun that will clear user token from SP

                _mutableLogoutState.value = LogoutResult.Success()
            }
        }
    }

    fun checkUserIsLoggedIn(): Boolean {
        // TODO check sharedPreferences is it has saved user token
        return TODO()
    }

    companion object {
        const val DELAY_MILLIS: Long = 3_000
    }
}

sealed class LoginResult {

    class Loading : LoginResult()

    class Success : LoginResult()

    sealed class Error : LoginResult() {

        class UserName : Error()

        class Password : Error()
    }
}

sealed class LogoutResult {

    class Loading : LogoutResult()

    class Success : LogoutResult()

    sealed class Error : LogoutResult()
}