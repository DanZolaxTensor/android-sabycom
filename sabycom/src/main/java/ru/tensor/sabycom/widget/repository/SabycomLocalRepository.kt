package ru.tensor.sabycom.widget.repository

import android.content.Context
import android.content.SharedPreferences
import ru.tensor.sabycom.Sabycom.NOT_INIT_ERROR
import ru.tensor.sabycom.data.UserData
import java.util.UUID

/**
 * @author ma.kolpakov
 */
internal class SabycomLocalRepository(context: Context) : LocalRepository {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun savePushToken(token: String) {
        sharedPreferences.edit().apply {
            putString(TOKEN_KEY, token)
        }.apply()
    }

    override fun getPushToken() = sharedPreferences.getString(TOKEN_KEY, null)

    override fun saveUser(user: UserData) {
        sharedPreferences.edit().apply {
            putString(USER_DATA_ID_KEY, user.id.toString())
            putStringIfNotNul(USER_DATA_NAME_KEY, user.name)
            putStringIfNotNul(USER_DATA_SURNAME_KEY, user.surname)
            putStringIfNotNul(USER_DATA_EMAIL_KEY, user.email)
            putStringIfNotNul(USER_DATA_PHONE_KEY, user.phone)
        }.apply()
    }

    override fun getUserData(): UserData {
        val userId = sharedPreferences.getString(USER_DATA_ID_KEY, null) ?: throw IllegalStateException(NOT_INIT_ERROR)
        return UserData(
            UUID.fromString(userId),
            sharedPreferences.getString(USER_DATA_NAME_KEY, null),
            sharedPreferences.getString(USER_DATA_SURNAME_KEY, null),
            sharedPreferences.getString(USER_DATA_EMAIL_KEY, null),
            sharedPreferences.getString(USER_DATA_PHONE_KEY, null)
        )
    }

    override fun saveApiKey(apiKey: String) {
        sharedPreferences.edit().apply {
            putString(API_KEY_KEY, apiKey)
        }.apply()
    }

    override fun getApiKey() = requireNotNull(sharedPreferences.getString(API_KEY_KEY, null)) { NOT_INIT_ERROR }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "SabycomSharedPreferences"
        private const val API_KEY_KEY = "API_KEY"
        private const val TOKEN_KEY = "TOKEN"
        private const val USER_DATA_ID_KEY = "USER_DATA_ID"
        private const val USER_DATA_NAME_KEY = "USER_DATA_NAME"
        private const val USER_DATA_SURNAME_KEY = "USER_DATA_SURNAME"
        private const val USER_DATA_EMAIL_KEY = "USER_DATA_EMAIL"
        private const val USER_DATA_PHONE_KEY = "USER_DATA_PHONE"
    }
}

private fun SharedPreferences.Editor.putStringIfNotNul(key: String, value: String?) {
    value?.let {
        putString(key, it)
    }
}
