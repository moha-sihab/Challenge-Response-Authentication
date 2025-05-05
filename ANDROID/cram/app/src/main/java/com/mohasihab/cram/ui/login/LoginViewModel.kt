package com.mohasihab.cram.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohasihab.cram.core.data.interfaces.LoginRepositoryContract
import com.mohasihab.cram.core.data.local.PreferenceManager
import com.mohasihab.cram.core.data.remote.request.LoginRequest
import com.mohasihab.cram.core.helper.PreferenceKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepositoryContract,
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState : StateFlow<LoginState> = _loginState

    fun login(username : String, password : String){
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val loginRequest = LoginRequest(username=username,password=password)
            val response = repository.login(loginRequest)
            if(response.success == true){
                response.data?.token.let { token ->
                    if(token != null){
                        preferenceManager.setString(PreferenceKeys.Auth.ACCESS_TOKEN,token)
                    }
                }

                preferenceManager.setInt(PreferenceKeys.User.USERID,response.data?.id ?: 0)
                preferenceManager.setString(PreferenceKeys.User.USERNAME,response.data?.username ?: "")

                _loginState.value = LoginState.Success(response.data?.username ?: "")
            }
            else{
                _loginState.value = LoginState.Error(response.message ?: "Unknown Error")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }

    fun checkLoginStatus() {
        viewModelScope.launch {
            val token = preferenceManager.getStringFlow(PreferenceKeys.Auth.ACCESS_TOKEN)
                .firstOrNull()

            val username = preferenceManager.getStringFlow(PreferenceKeys.User.USERNAME)
                .firstOrNull()

            if (token != null) {
                _loginState.value = LoginState.Success(username ?: "")
            } else {
                _loginState.value = LoginState.Idle
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val username: String) : LoginState()
    data class Error(val message: String) : LoginState()
}