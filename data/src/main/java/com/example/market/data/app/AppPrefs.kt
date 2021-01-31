package com.example.market.data.app

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.example.market.data.app.AppPrefs.PreferencesKeys.DEVICE_ID
import com.example.market.data.app.AppPrefs.PreferencesKeys.IS_LOGIN
import com.example.market.data.app.AppPrefs.PreferencesKeys.MOBILE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


const val STORE_SETTINGS = "settings"

class AppPrefs(private val dataStore: DataStore<Preferences>) {

    suspend fun setIsLogin(isLogin: Boolean) {
        dataStore.edit {
            it[IS_LOGIN] = isLogin
        }
    }

    suspend fun isLogin(): Flow<Boolean> = dataStore.data.map { it[IS_LOGIN] ?: false }

    suspend fun setDeviceId(deviceId: String) {
        dataStore.edit {
            it[DEVICE_ID] = deviceId
        }
    }

    suspend fun getDeviceId(): Flow<String> =
        dataStore.data.map { it[DEVICE_ID] ?: "" }


    suspend fun setMobile(phoneNumber: String): Flow<String> {
        dataStore.edit {
            it[MOBILE] = phoneNumber
        }
        return dataStore.data.map { it[MOBILE] ?: "" }
    }

    suspend fun getMobile(): Flow<String> = dataStore.data.map { it[MOBILE] ?: "" }

    private object PreferencesKeys {
        val MOBILE = preferencesKey<String>("mobile")
        val DEVICE_ID = preferencesKey<String>("device_id")
        val IS_LOGIN = preferencesKey<Boolean>("is_login")
    }
}