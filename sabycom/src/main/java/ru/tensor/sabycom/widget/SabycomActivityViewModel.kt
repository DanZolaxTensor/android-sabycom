package ru.tensor.sabycom.widget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.Sabycom.NOT_INIT_ERROR

/**
 * @author ma.kolpakov
 */
internal class SabycomActivityViewModel : ViewModel() {

    private val repository = Sabycom.repository
    private var stateLiveData = MutableLiveData<WebWidgetState>()
    var state: LiveData<WebWidgetState> = stateLiveData

    init {
        stateLiveData.value = WebWidgetState.OPENED
        Sabycom.sabycomFeature?.onClose = {
            stateLiveData.value = WebWidgetState.CLOSED
        }
    }

    fun getUser() = checkNotNull(repository.registerData?.user) { NO_USER_DATA_ERROR }

    fun getApiKey() = checkNotNull(repository.registerData?.apiKey) {NOT_INIT_ERROR}
}

private const val NO_USER_DATA_ERROR =
    "Before showing widget, you need to register the user by calling method [Sabycom.registerUser(<user data>)]"