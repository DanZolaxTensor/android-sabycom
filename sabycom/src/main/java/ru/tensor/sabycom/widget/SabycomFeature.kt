package ru.tensor.sabycom.widget

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ru.tensor.sabycom.data.UserData

/**
 * @author ma.kolpakov
 */
internal class SabycomFeature(apiKey: String) {

    private val urlString = "https://test-consultant.sbis.ru/consultant/$apiKey"
    private val hideEvent = MutableLiveData<Unit>()
    private var userData: UserData? = null

    fun show(activity: AppCompatActivity) {
        val sabycomDialog = SabycomDialog.newInstance(urlString, checkNotNull(userData) { NO_USER_DATA_ERROR })

        hideEvent.observeOnce(activity) {
            sabycomDialog.dismiss()
        }

        sabycomDialog.show(activity.supportFragmentManager, SABYCOM_TAG)
    }

    fun hide() {
        hideEvent.value = Unit
    }

    fun registerUser(userData: UserData) {
        this.userData = userData
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}

private const val SABYCOM_TAG = "Sabycom"
private const val NO_USER_DATA_ERROR =
    "Before showing widget, you need to register the user by calling method [Sabycom.registerUser(<user data>)]"