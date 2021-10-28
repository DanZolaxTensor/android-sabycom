package ru.tensor.sabycom.widget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.Sabycom.NOT_INIT_ERROR
import ru.tensor.sabycom.data.UrlUtil
import ru.tensor.sabycom.widget.repository.RemoteRepository

/**
 * @author ma.kolpakov
 */
internal class SabycomActivityViewModel(private val repository: RemoteRepository = Sabycom.repository) : ViewModel() {

    private val stateLiveData = MutableLiveData<WebWidgetState>()
    val state: LiveData<WebWidgetState> = stateLiveData

    init {
        stateLiveData.value = Opened(
            UrlUtil.buildWidgetUrl(
                userId = getUser().id.toString(),
                apiKey = getApiKey()
            ),
            getUser()
        )
        Sabycom.sabycomFeature?.onClose = {
            stateLiveData.value = Closed
        }
    }

    override fun onCleared() {
        Sabycom.sabycomFeature?.onClose = null
    }

    private fun getUser() = checkNotNull(repository.registerData?.user) { NO_USER_DATA_ERROR }

    private fun getApiKey() = checkNotNull(repository.registerData?.apiKey) { NOT_INIT_ERROR }

    companion object {
        private const val NO_USER_DATA_ERROR =
            "Before showing widget, you need to register the user by calling method [Sabycom.registerUser(<user data>)]"
    }
}
