package ru.tensor.sabycomdemo

import android.app.Application
import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import ru.tensor.sabycom.Sabycom

/**
 * @author ma.kolpakov
 */
class SabycomApp : Application() {
    override fun onCreate() {
        super.onCreate()
        getSharedPreferences(SABYCOM_HOST_PREFS, Context.MODE_PRIVATE).getString(APP_ID_KEY, DEFAULT_APP_ID)?.let {
            Sabycom.initialize(applicationContext, it)
        }
        refreshToken()
    }

    private fun refreshToken() {
        FirebaseMessaging.getInstance()
            .token
            .addOnSuccessListener { token ->
                Sabycom.sendToken(token)
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }

    internal companion object {
        const val SABYCOM_HOST_PREFS = "SABYCOM_HOST_PREFS"
        const val CURRENT_HOST_KEY = "CURRENT_HOST_KEY"
        const val APP_ID_KEY = "APP_ID_KEY"
        const val DEFAULT_APP_ID = "b956719f-965c-4204-815e-89894ba2aa9e"
    }
}