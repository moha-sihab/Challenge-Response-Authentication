package com.mohasihab.cram.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohasihab.cram.core.data.interfaces.UserRepositoryContract
import com.mohasihab.cram.core.data.local.PreferenceManager
import com.mohasihab.cram.core.data.remote.request.PublicKeyRequest
import com.mohasihab.cram.core.helper.PreferenceKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserRepositoryContract,
    private val preferenceManager: PreferenceManager
) : ViewModel() {
    private val _homeState = MutableStateFlow<HomeState>(HomeState.Idle)
    val homeState = _homeState
    fun sendPublicKey(publicKey: String){
        viewModelScope.launch {
            val userId = preferenceManager.getIntFlow(PreferenceKeys.User.USERID).firstOrNull()
            val publicKeyRequest = PublicKeyRequest(publicKey = publicKey)
            repository.sendPublicKey(userId ?: 0, publicKeyRequest)

        }
    }

    fun logout(){
        viewModelScope.launch {
            preferenceManager.remove(PreferenceKeys.Auth.ACCESS_TOKEN)
            preferenceManager.remove(PreferenceKeys.User.USERNAME)
        }
    }

    fun getUser(){
        _homeState.value = HomeState.Loading
        viewModelScope.launch {
            val username = preferenceManager.getStringFlow(PreferenceKeys.User.USERNAME).firstOrNull()
            if(username != null){
                _homeState.value = HomeState.Success(username)
            }
        }
    }

}

sealed class HomeState {
    object Idle : HomeState()
    object Loading : HomeState()
    data class Success(val username: String) : HomeState()
    data class Error(val message: String) : HomeState()
}