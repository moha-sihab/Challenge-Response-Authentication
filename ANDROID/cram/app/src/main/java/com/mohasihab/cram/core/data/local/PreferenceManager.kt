package com.mohasihab.cram.core.data.local

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import androidx.core.content.edit

class PreferenceManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun getStringFlow(key: String): Flow<String?> = flow {
        emit(sharedPreferences.getString(key, null))
    }

    fun getIntFlow(key: String): Flow<Int?> = flow {
        val value = if (sharedPreferences.contains(key)) sharedPreferences.getInt(key, 0) else null
        emit(value)
    }

    fun getString(key: String): String? = sharedPreferences.getString(key, null)

    fun getInt(key: String): Int? =
        if (sharedPreferences.contains(key)) sharedPreferences.getInt(key, 0) else null


    fun setString(key: String, value: String) {
        sharedPreferences.edit() { putString(key, value) }
    }

    fun setInt(key: String, value: Int) {
        sharedPreferences.edit() { putInt(key, value) }
    }

    fun remove(key: String) {
        sharedPreferences.edit() { remove(key) }
    }

    fun clearAll() {
        sharedPreferences.edit() { clear() }
    }
}