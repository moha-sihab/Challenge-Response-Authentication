package com.mohasihab.cram.ui.login

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohasihab.cram.core.data.interfaces.ChallengeRepositoryContract
import com.mohasihab.cram.core.data.interfaces.LoginRepositoryContract
import com.mohasihab.cram.core.data.local.PreferenceManager
import com.mohasihab.cram.core.data.remote.request.ChallengeResponseRequest
import com.mohasihab.cram.core.data.remote.request.LoginRequest
import com.mohasihab.cram.core.data.remote.response.BaseResponse
import com.mohasihab.cram.core.data.remote.response.LoginResponse
import com.mohasihab.cram.core.helper.BiometricHelper.signDataWithRSA
import com.mohasihab.cram.core.helper.PreferenceKeys
import com.mohasihab.cram.core.helper.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepositoryContract,
    private val challengeRepository: ChallengeRepositoryContract,
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    private val _loginState : MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val loginState : StateFlow<LoginState> = _loginState

    fun login(username : String, password : String){
        _loginState.value = _loginState.value.copy(uiState = UiState.Loading)
        viewModelScope.launch {
            val loginRequest = LoginRequest(username=username,password=password)
            val response = loginRepository.login(loginRequest)
            if(response.success == true){
                response.data?.token.let { token ->
                    if(token != null){
                        preferenceManager.setString(PreferenceKeys.Auth.ACCESS_TOKEN,token)
                    }
                }

                preferenceManager.setInt(PreferenceKeys.User.USERID,response.data?.id ?: 0)
                preferenceManager.setString(PreferenceKeys.User.USERNAME,response.data?.username ?: "")

                _loginState.value =_loginState.value.copy(uiState = UiState.Success(response))
            }
            else{
                _loginState.value =_loginState.value.copy(uiState = UiState.Error(response.message ?: "Unknown Error"))
            }
        }
    }

    fun resetState() {
        _loginState.value = _loginState.value.copy(uiState = UiState.Idle)
    }

    fun checkLoginStatus() {
        viewModelScope.launch {
            val token = preferenceManager.getStringFlow(PreferenceKeys.Auth.ACCESS_TOKEN)
                .firstOrNull()

            val username = preferenceManager.getStringFlow(PreferenceKeys.User.USERNAME)
                .firstOrNull()

            val userId = preferenceManager.getIntFlow(PreferenceKeys.User.USERID).firstOrNull()

            if (token != null) {
                _loginState.value = _loginState.value.copy(uiState = UiState.Success(BaseResponse(data = LoginResponse(token = token, username = username, id = userId))))
            } else {
                _loginState.value = _loginState.value.copy(uiState = UiState.Idle)
            }
        }
    }

    fun onBiometricSuccess() {
        _loginState.value = _loginState.value.copy(uiState = UiState.Loading)
        viewModelScope.launch {
            try {
                var userId = preferenceManager.getInt(PreferenceKeys.User.USERID)
                if (userId != null){
                    val challenge = challengeRepository.createChallenge(userId)
                    val challengeText = challenge.data?.challengeText

                    if(challengeText != null){
                        val challengeBytes =challengeText.toByteArray(Charsets.UTF_8)
                        val signed = signDataWithRSA(challengeBytes)
                        val signatureBase64 = Base64.encodeToString(signed, Base64.NO_WRAP)

                        Log.d("CRAM", "challengeText: $challengeText")
                        Log.d("CRAM", "signed (Base64): $signatureBase64")

                        val response = challenge.data.id?.let {
                            challengeRepository.responseChallenge(
                                ChallengeResponseRequest(
                                    challengeId = it,
                                    signature = signatureBase64,
                                    userId = userId
                                )
                            )
                        }

                        if(response?.success == true){
                            response.data?.token.let { token ->
                                if(token != null){
                                    preferenceManager.setString(PreferenceKeys.Auth.ACCESS_TOKEN,token)
                                }
                            }

                            preferenceManager.setInt(PreferenceKeys.User.USERID,response.data?.id ?: 0)
                            preferenceManager.setString(PreferenceKeys.User.USERNAME,response.data?.username ?: "")

                            _loginState.value =_loginState.value.copy(uiState = UiState.Success(response))
                        }
                        else{
                            _loginState.value =_loginState.value.copy(uiState = UiState.Error(response?.message ?: "Unknown Error"))
                        }
                    }else{
                        _loginState.value =_loginState.value.copy(uiState = UiState.Error("Challenge text is empty"))
                    }

                }

            } catch (e: Exception) {
                _loginState.value =_loginState.value.copy(uiState = UiState.Error(e.message ?: "Unknown Error"))
            }
        }
    }
}

data class LoginState(
    val uiState: UiState<BaseResponse<LoginResponse>> = UiState.Idle,
)