package ru.tensor.sabycom.widget

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.data.UrlUtil
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycom.widget.repository.Repository

/**
 * @author ma.kolpakov
 */
internal class SabycomActivityViewModel(application: Application, repository: Repository = Sabycom.repository) :
    AndroidViewModel(application) {
    private val _openEvent = MutableLiveData<OpenWidgetData>()
    val openEvent: LiveData<OpenWidgetData> = _openEvent

    private val _closeEvent = MutableLiveData<Unit>()
    val closeEvent: LiveData<Unit> = _closeEvent

    private val _internetAvailable = MutableLiveData<Boolean>()
    val internetAvailable: LiveData<Boolean> = _internetAvailable


    private val _pageReady = MutableLiveData<Unit>()
    val pageReady: LiveData<Unit> = _pageReady


    private var connectivityManager: ConnectivityManager? = null

    private var networkWatcher: NetworkWatcher? = null

    init {
        _openEvent.value = OpenWidgetData(
            UrlUtil.buildWidgetUrl(
                userId = repository.requireUserData().id.toString(),
                apiKey = repository.requireApiKey()
            ),
            repository.requireUserData(),
            repository.requireApiKey()
        )
        Sabycom.sabycomFeature?.onClose = {
            hide()
        }
        connectivityManager = application.getSystemService(ConnectivityManager::class.java)

        networkWatcher = NetworkWatcher() {
            _internetAvailable.postValue(it)
        }
        connectivityManager?.registerDefaultNetworkCallback(networkWatcher!!)
    }


    override fun onCleared() {
        Sabycom.sabycomFeature?.onClose = null
        connectivityManager?.unregisterNetworkCallback(networkWatcher!!)
        networkWatcher = null
        connectivityManager = null
    }

    fun hide() {
        _closeEvent.postValue(Unit)
    }

    fun showWebView() {
        _pageReady.postValue(Unit)
    }

    internal fun isNetworkAvailable() = networkWatcher!!.isAvailable

    internal data class OpenWidgetData(val url: String, val userData: UserData, val channel: String)
}
