package ru.tensor.sabycom.widget

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.data.UrlUtil
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycom.widget.repository.Repository

/**
 * @author ma.kolpakov
 */
internal class SabycomActivityViewModel(private val repository: Repository = Sabycom.repository) : ViewModel() {

    private val openEventLiveData = MutableLiveData<OpenWidgetData>()
    val openEvent: LiveData<OpenWidgetData> = openEventLiveData
    private val closeEventLiveData = MutableLiveData<Unit>()
    val closeEvent: LiveData<Unit> = closeEventLiveData

    init {
        openEventLiveData.value = OpenWidgetData(
            UrlUtil.buildWidgetUrl(
                userId = repository.getUserData().id.toString(),
                apiKey = repository.getApiKey()
            ),
            repository.getUserData()
        )
        Sabycom.sabycomFeature?.onClose = {
            hide()
        }
    }

    override fun onCleared() {
        Sabycom.sabycomFeature?.onClose = null
    }

    @MainThread
    fun hide() {
        closeEventLiveData.value = Unit
    }

    companion object {
        private const val NO_USER_DATA_ERROR =
            "Before showing widget, you need to register the user by calling method [Sabycom.registerUser(<user data>)]"
    }

    internal data class OpenWidgetData(val url: String, val userData: UserData)
}
