package ru.tensor.sabycom.widget

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycom.widget.repository.RemoteRepository

/**
 * @author ma.kolpakov
 */
internal class SabycomFeature(
    private val apiKey: String,
    private val repository: RemoteRepository
) {
    internal var onClose: (() -> Unit)? = null

    fun show(activity: AppCompatActivity) {
        activity.startActivity(SabycomActivity.getIntent(activity))
    }

    fun hide() {
        onClose?.invoke()
    }

    fun registerUser(userData: UserData) {
        repository.registerUser(userData, apiKey)
    }
}
