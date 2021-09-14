package ru.tenzor.sabycom.widget

import android.util.Log
import android.webkit.CookieManager
import androidx.appcompat.app.AppCompatActivity
import ru.tenzor.sabycom.data.UserData
import ru.tenzor.sabycom.utils.mergeCookies
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.Executors

/**
 * @author ma.kolpakov
 */
internal class SabycomController(apiKey: String) {

    private val urlString = "https://test-consultant.sbis.ru/consultant/$apiKey"
    private var sabycomDialog: SabycomDialog? = null
    private var preloadedData: String? = null
    private var userData: UserData? = null
    fun preload() {
        checkNotNull(userData) { NO_USER_DATA_ERROR }
        saveUrlData()
    }

    private val mSingleThreadExecutor = Executors.newSingleThreadExecutor()
    fun show(activity: AppCompatActivity) {
        checkNotNull(userData) { NO_USER_DATA_ERROR }
        sabycomDialog = SabycomDialog.newInstance(urlString, preloadedData, userData)
        sabycomDialog?.show(activity.supportFragmentManager, SABYCOM_TAG)
    }

    fun hide() {
        sabycomDialog?.dismiss()
    }

    fun registerUser(userData: UserData) {
        this.userData = userData
    }

    private fun saveUrlData() {
        mSingleThreadExecutor.submit {
            try {
                val url = URL(urlString)
                val openConnection = url.openConnection()
                openConnection.connect()

                val newCookie = openConnection.headerFields["Set-Cookie"]?.find { it.contains("cookie_check") }

                newCookie?.let {
                    val oldCookiesString = CookieManager.getInstance().getCookie(urlString)
                    CookieManager.getInstance().setCookie(urlString, mergeCookies(oldCookiesString, it))
                }

                val bufferedReader = BufferedReader(InputStreamReader(openConnection.getInputStream()))
                var input: String?
                val stringBuffer = StringBuffer()
                while (bufferedReader.readLine().also { input = it } != null) {
                    stringBuffer.append(input)
                }
                bufferedReader.close()
                preloadedData = stringBuffer.toString()
            } catch (e: Exception) {
                Log.e(SABYCOM_TAG, "Can't preload Sabycom page", e)
            }
        }
    }

}

private const val SABYCOM_TAG = "Sabycom"
private const val NO_USER_DATA_ERROR = "Sabycom"