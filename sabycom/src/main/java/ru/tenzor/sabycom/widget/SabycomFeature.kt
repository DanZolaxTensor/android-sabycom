package ru.tenzor.sabycom.widget

import androidx.appcompat.app.AppCompatActivity
import ru.tenzor.sabycom.data.UserData

/**
 * @author ma.kolpakov
 */
internal class SabycomFeature(apiKey: String) {

    private val urlString = "https://test-consultant.sbis.ru/consultant/$apiKey"
    private var sabycomDialog: SabycomDialog? = null
    private var userData: UserData? = null

    fun show(activity: AppCompatActivity) {
        sabycomDialog = SabycomDialog.newInstance(urlString, checkNotNull(userData) { NO_USER_DATA_ERROR })
        sabycomDialog?.show(activity.supportFragmentManager, SABYCOM_TAG)
    }

    fun hide() {
        sabycomDialog?.dismiss()
    }

    fun registerUser(userData: UserData) {
        this.userData = userData
    }

}

private const val SABYCOM_TAG = "Sabycom"
private const val NO_USER_DATA_ERROR = "Sabycom"