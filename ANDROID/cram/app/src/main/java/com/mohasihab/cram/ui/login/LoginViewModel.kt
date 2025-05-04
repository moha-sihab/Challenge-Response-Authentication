package com.mohasihab.cram.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohasihab.cram.core.data.interfaces.LoginRepositoryContract
import com.mohasihab.cram.core.data.local.TokenManager
import com.mohasihab.cram.core.data.remote.request.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepositoryContract,
    private val tokenManager: TokenManager
) : ViewModel() {
    fun login(username : String, password : String){
        viewModelScope.launch {
            val loginRequest = LoginRequest(username=username,password=password)
            val response = repository.login(loginRequest)
            if(response.success == true){
                response.data?.token.let { token ->
                    if(token != null){
                        tokenManager.saveToken(token)
                    }
                }
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