package com.mohasihab.cram.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohasihab.cram.core.data.interfaces.UserRepositoryContract
import com.mohasihab.cram.core.data.local.PreferenceManager
import com.mohasihab.cram.core.data.remote.request.PublicKeyRequest
import com.mohasihab.cram.core.helper.PreferenceKeys
import com.mohasihab.cram.core.helper.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserRepositoryContract,
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    private val _homeState : MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val homeState = _homeState
    fun sendPublicKey(publicKey: String){
        viewModelScope.launch {
            val userId = preferenceManager.getIntFlow(PreferenceKeys.User.USERID).firstOrNull()
            val publicKeyRequest = PublicKeyRequest(publicKey = publicKey)
            val response = repository.sendPublicKey(userId ?: 0, publicKeyRequest)
            if(response.success == true){
                _homeState.value = _homeState.value.copy(successPublicKey = true)
            }else{
                _homeState.value = _homeState.value.copy(successPublicKey = false)
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            preferenceManager.remove(PreferenceKeys.Auth.ACCESS_TOKEN)
            preferenceManager.remove(PreferenceKeys.User.USERNAME)
        }
    }

    fun getUser(){
        _homeState.value = _homeState.value.copy(uiState = UiState.Loading)
        viewModelScope.launch {
            val username = preferenceManager.getStringFlow(PreferenceKeys.User.USERNAME).firstOrNull()
            if(username != null){
                _homeState.value = _homeState.value.copy(uiState = UiState.Success(username))
            }
        }
    }

}

data class HomeState (
    val uiState: UiState<String> = UiState.Idle,
    val successPublicKey: Boolean = false,
)