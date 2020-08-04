package com.emdad.loginapplicationmvvm.ui.login

import androidx.core.os.postDelayed
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.logging.Handler

class LoginViewModel : ViewModel() {
    var state: LoginViewState = LoginViewState.Idle
    private val _stateLiveData = MutableLiveData<LoginViewState>(LoginViewState.Idle)

    val stateLiveData = _stateLiveData

    fun onSubmit(email: String, password: String) {
        when {
            isInvalidValid(email) -> _stateLiveData.value = LoginViewState.Failed(
                message = "Email is invalid"
            )
            isPasswordInvalid(password) -> _stateLiveData.value = LoginViewState.Failed(
                message = "Password is invalid"
            )
            else -> {
                _stateLiveData.value = LoginViewState.Progress
                processLogin { hasSucceed ->
                    if (hasSucceed) {
                        _stateLiveData.value = LoginViewState.Succeed("Yay welcome back")
                    } else {
                        _stateLiveData.value = LoginViewState.Failed("Login has failed")
                    }
                }

            }

        }

    }

    private fun processLogin(callBack: (Boolean) -> Unit) {
        android.os.Handler().postDelayed(2000) {
            callBack(true)
        }
    }

    private fun isPasswordInvalid(password: String): Boolean {
        return password.count() < 4
    }

    private fun isInvalidValid(email: String): Boolean {
        return email.singleOrNull { it == '@' } == null
    }


}

sealed class LoginViewState {
    object Idle : LoginViewState()

    object Progress : LoginViewState()

    data class Failed(val message: String) : LoginViewState()

    data class Succeed(val message: String) : LoginViewState()


}