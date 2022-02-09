package ru.tensor.sabycom.widget

import android.net.ConnectivityManager
import android.net.Network

/**
 * @author ma.kolpakov
 */
internal class NetworkWatcher(private val listener: (Boolean) -> Unit) : ConnectivityManager.NetworkCallback() {
    var isAvailable: Boolean = false
        private set

    override fun onAvailable(network: Network) {
        if (!isAvailable) {
            listener(true)
        }
        isAvailable = true
        super.onAvailable(network)
    }

    override fun onLost(network: Network) {
        if (isAvailable) {
            listener(false)
        }
        isAvailable = false
        super.onLost(network)
    }
}