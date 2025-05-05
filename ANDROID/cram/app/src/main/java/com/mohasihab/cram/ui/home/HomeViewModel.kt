package com.mohasihab.cram.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohasihab.cram.core.data.interfaces.UserRepositoryContract
import com.mohasihab.cram.core.data.local.PreferenceManager
import com.mohasihab.cram.core.data.remote.request.PublicKeyRequest
import com.mohasihab.cram.core.helper.PreferenceKeys
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserRepositoryContract,
    private val preferenceManager: PreferenceManager
) : ViewModel() {
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

    fun checkToken(){
        viewModelScope.launch {
            val token = preferenceManager.getStringFlow(PreferenceKeys.Auth.ACCESS_TOKEN).firstOrNull()
            Log.d("AuthInterceptor", "Token: $token")

        }

    }
}